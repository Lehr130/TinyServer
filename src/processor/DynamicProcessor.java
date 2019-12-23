package processor;

import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import bean.MyMethod;
import bean.MyRequest;
import bean.ParsedResult;
import bean.ProcessedData;
import container.ContainerFacade;
import exceptions.CannotFindException;
import exceptions.ParamException;
import message.Code;
import message.RequestType;


/**
 * 
 * @author Lehr
 * @date 2019年12月23日
 * 
 */
@SuppressWarnings("rawtypes") 
public class DynamicProcessor extends Processor {

	@Override
	protected ProcessedData prepareData(Socket socket, MyRequest request, ParsedResult parsedResult) throws Exception {

		ContainerFacade container = ContainerFacade.getInstance();

		HashMap<String, String> inputParamMap;

		// 如果不是GET方法，那么参数就是从请求体里来的
		if (request.getRequestType() != RequestType.GET) {
			inputParamMap = request.getBodyList();
		} else {
			// 不然的话（GET请求）就去查GET后面Uri参数
			inputParamMap = parsedResult.getParams();
		}

		// 获取用户输入的参数列表

		// 获取方法相关的信息，按照uri和请求方法去找，若找不到就报错返回
		MyMethod myMethod = container.getMethod(parsedResult.getParseUri(), request.getRequestType());

		if (myMethod == null) {
			throw new CannotFindException("Cannot Find Method By Your Uri or Request Type");
		}

		// 获取可执行的这个方法本体
		Method method = myMethod.getMethod();

		// 检查参数----个数检查
		Integer paraCount = method.getParameterCount();

		/*
		 * 来我翻译一下这段辣鸡代码： 如果入参为0但是标准参数表中不是0个 或者 入参个数和标准参数表里的不一样 就抛错
		 * 
		 * 这里我感觉用null确实不太好，到时候炸NTE了.....
		 */
		if ((inputParamMap == null && paraCount != 0) || (inputParamMap != null && paraCount != inputParamMap.size())) {
			throw new ParamException("The Number of Input Parameters Is Wrong!");
		}

		// 调整入参位置和转型 //把这个方法写进来
		Object[] paramArray = setParam(inputParamMap, myMethod, paraCount);

		// 规定了必须是静态方法所以我这里第一个参数就是null了
		Object ret = method.invoke(null, paramArray);

		byte[] bytes = null;

		if (ret != null) {
			bytes = String.valueOf(ret).getBytes();
		}

		// 这个fileType要改，然后就是响应码要随着请求类型而改变
		return new ProcessedData("text/html", bytes, Code.OK);
	}

	/**
	 * 通过输入的参数数组，和已知的标准参数列表进行对比并赋值，若不成功就爆错
	 * 
	 * @param inputParamMap
	 * @param myMethod
	 * @param paraCount
	 * @return
	 * @throws ParamException
	 */
	private Object[] setParam(Map<String, String> inputParamMap, MyMethod myMethod, Integer paraCount)
			throws ParamException {

		// 如果没有参数需求，就无需校正了
		if (inputParamMap == null) {
			return new Object[0];
		}

		// 准备：最终会传入的参数数组
		Object[] paramArray = new Object[paraCount];

		// 导入标准的参数名称和类
		HashMap<String, Class> standardParamMap = myMethod.getParaMap();

		// 参数数组计数器
		Integer i = 0;

		// 检擦是否每一个参数都被传递到了，类是否正确
		for (Entry<String, Class> entry : standardParamMap.entrySet()) {

			Class type = entry.getValue();

			String paramResult = inputParamMap.get(entry.getKey());

			if (paramResult == null) {
				throw new ParamException("Unknow Parameter has been Input!");
			}

			// 只提供几种常见的转型先？这里以后还要分出来写
			// 自动转型的垃圾代码......
			paramArray[i++] = tranType(paramResult, type);

		}

		// 返回调整后的参数列表
		return paramArray;
	}

	/**
	 * 专门用来处理参数转型的类，配了一个超级垃圾的正则表达式
	 * 
	 * @param paramResult
	 * @param type
	 * @return
	 * @throws ParamException
	 */
	private Object tranType(String paramResult, Class type) throws ParamException {

		if (type == Integer.class) {

			if (!paramResult.matches("[0-9]+")) {
				throw new ParamException("Parameter Type is Incorrect!!!");
			}

			return Integer.parseInt(paramResult);
		}
		if (type == Double.class) {

			if (paramResult.matches("[0-9]*[.]?[0-9]+?")) {
				throw new ParamException("Parameter Type is Incorrect!!");
			}

			return Double.parseDouble(paramResult);
		}
		if (type == Float.class) {

			if (paramResult.matches("[0-9]*[.]?[0-9]+?")) {
				throw new ParamException("Parameter Type is Incorrect!");
			}

			return Float.parseFloat(paramResult);
		}

		return type.cast(paramResult);

	}

}

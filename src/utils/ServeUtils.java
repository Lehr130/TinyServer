package utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import bean.MyMethod;
import bean.MyRequest;
import bean.MyResponse;
import bean.ParsedResult;
import exceptions.CannotFindMethod;
import exceptions.ParamException;
import main.dynamic.Router;
import message.Code;
import server.MyCache;

/**
 * 
 * @author Lehr
 * @date 2019年12月17日
 * 
 */
@SuppressWarnings("rawtypes")
public class ServeUtils {

	/**
	 * 反正根据那个什么含蓄类原则，这种全是static的工具类要不能暴露new方法......
	 */
	private ServeUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 响应动态方法,通过传入的请求和解析请求uri的结果（获取参数）,来进行动态方法调用
	 * @param request
	 * @param result
	 * @param socket
	 * @param router
	 * @throws CannotFindMethod
	 * @throws ParamException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void serverDynamic(MyRequest request, ParsedResult result, Socket socket, Router router)
			throws CannotFindMethod, ParamException, IllegalAccessException, InvocationTargetException {

		// 获取用户输入的参数列表
		HashMap<String, String> inputParamMap = result.getParams();

		// 获取方法相关的信息，按照uri和请求方法去找，若找不到就报错返回
		MyMethod myMethod = router.getMethod(result.getParseUri(), request.getRequestType());

		if (myMethod == null) {
			throw new CannotFindMethod("Cannot Find Method By Your Uri or Request Type");
		}

		// 获取可执行的这个方法本体
		Method method = myMethod.getMethod();

		// 检查参数----个数检查
		Integer paraCount = method.getParameterCount();

		if ((inputParamMap == null && paraCount != 0) || (inputParamMap != null && paraCount != inputParamMap.size())) {
			throw new ParamException("The Number of Input Parameters Is Wrong!");
		}

		// 调整入参位置
		Object[] paramArray = setParam(inputParamMap, myMethod, paraCount);

		// 规定了必须是静态方法所以我这里第一个参数就是null了
		Object ret = method.invoke(null, paramArray);

		byte[] bytes = null;

		if (ret != null) {
			bytes = String.valueOf(ret).getBytes();
		}

		//获取动态方法的响应结果并封装返回
		sendResponse(socket, request.getVersion(), "text/html", bytes, Code.OK);

	}

	/**
	 * 响应静态资源请求，默认只能是GET方法的，且有缓存机制
	 * @param filename
	 * @param socket
	 * @param version
	 * @param cache
	 * @throws IOException
	 */
	public static void serverStatic(String filename, Socket socket, String version, MyCache cache) throws IOException {

		// out方法不一定会及时输出，err更方便debug，可以及时输出，常见场景：循环出错
		System.err.println(filename);

		// 获取文件类型
		String fileType = UrlUtils.getFileType(filename);

		//尝试获取到缓存的内容
		byte[] fileContent = cache.checkCache(filename);
		
		// 如果缓存里没有就再去目录里解析文件
		if (fileContent == null) {

			//去找
			fileContent = FileUtils.fileToByte(filename);
			
			//解析成功后放入缓存
			cache.putIntoCache(filename, fileContent);

		}

		//发送响应结果
		sendResponse(socket, version, fileType, fileContent, Code.OK);

	}

	/**
	 * 当发送异常的时候返回错误提示页面（以后可以扩展这里）
	 * @param socket
	 * @param version
	 * @param code
	 * @param cache
	 */
	public static void clientError(Socket socket, String version, String code, MyCache cache){

		// 向服务器记录报错信息
		System.err.println("fuck that!");

		// 获取报错页面并去响应
		sendResponse(socket, version, "text/html", cache.getErrorCache(code), code);

	}

	/**
	 * 发送响应
	 * @param socket
	 * @param version
	 * @param fileType
	 * @param fileContent
	 * @param code
	 */
	public static void sendResponse(Socket socket, String version, String fileType, byte[] fileContent, String code) {

		//把所有的数据聚合成一个响应
		MyResponse res = new MyResponse(version, code, fileType, fileContent);

		//并发送响应
		res.sendResponse(socket);

	}

	
	/**
	 * 通过输入的参数数组，和已知的标准参数列表进行对比并赋值，若不成功就爆错
	 * @param inputParamMap
	 * @param myMethod
	 * @param paraCount
	 * @return
	 * @throws ParamException
	 */
	public static Object[] setParam(Map<String, String> inputParamMap, MyMethod myMethod, Integer paraCount)
			throws ParamException {

		// 如果没有参数需求，就无需校正了
		if (inputParamMap == null) {
			return new Object[0];
		}

		// 准备：最终会传入的参数数组
		Object[] paramArray = new Object[paraCount];

		// 导入标准的参数名称和类
		HashMap<String, Class> standardParamMap = myMethod.getParaMap();

		Integer i = 0;

		// 检擦是否每一个参数都被传递到了，类是否正确
		for (Entry<String, Class> entry : standardParamMap.entrySet()) {

			Class type = entry.getValue();

			String paramResult = inputParamMap.get(entry.getKey());

			if (paramResult == null) {
				throw new ParamException("Unknow Parameter has been Input!");
			}

			// 只提供几种常见的转型先？这里以后还要分出来写
			if (type == Integer.class) {
				paramArray[i++] = Integer.parseInt(paramResult);
			} else if (type == Double.class) {
				paramArray[i++] = Double.parseDouble(paramResult);
			} else if (type == Float.class) {
				paramArray[i++] = Float.parseFloat(paramResult);
			} else {
				paramArray[i++] = type.cast(paramResult);
			}

		}

		//返回调整后的参数列表
		return paramArray;
	}
}
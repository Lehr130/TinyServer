package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import bean.MyMethod;
import exceptions.ParamException;

/**
 * 	专门用于负责处理各种算法的
 * @author Lehr
 * @date 2019年12月22日
 * 
 */
@SuppressWarnings("rawtypes") 
public class AlgorithmUtils {

	/**
	 * 专门用来处理参数转型的类，配了一个超级垃圾的正则表达式
	 * @param paramResult
	 * @param type
	 * @return
	 * @throws ParamException
	 */
	public static Object tranType(String paramResult, Class type) throws ParamException {

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
	
	/**
	 * 通过输入的参数数组，和已知的标准参数列表进行对比并赋值，若不成功就爆错
	 * 
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
			paramArray[i++] = tranType(paramResult,type);

		}

		// 返回调整后的参数列表
		return paramArray;
	}


}

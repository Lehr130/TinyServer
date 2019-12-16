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

	public static void serverDynamic(MyRequest request, ParsedResult result, Socket socket, Router router)
			throws CannotFindMethod, ParamException, IllegalAccessException, InvocationTargetException, IOException {

		// 获取参数列表
		HashMap<String, String> inputParamMap = result.getParams();

		// 获取方法
		MyMethod myMethod = router.getMethod(result.getParseUri(), request.getRequestType());

		if (myMethod == null) {
			throw new CannotFindMethod("Cannot Find Method By Your Uri");
		}

		// 获取方法本体
		Method method = myMethod.getMethod();

		// 现在能获取到注册了了的方法了,所以我们需要做的就是灌入参数，得到返回值

		// 检查参数----个数检查
		Integer paraCount = method.getParameterCount();

		if ((inputParamMap == null && paraCount != 0) || (inputParamMap != null && paraCount != inputParamMap.size())) {
			throw new ParamException("The Number of Input Parameters Is Wrong!");
		}

		// 调整入参位置
		Object[] paramArray = setParam(inputParamMap, myMethod, paraCount);

		// 规定了必须是静态方法所以我这里第一个参数就是null了
		// 这里转型会出问题
		Object ret = method.invoke(null, paramArray);

		byte[] bytes = null;

		if (ret != null) {
			bytes = String.valueOf(ret).getBytes();
		}

		sendResponse(socket, request.getVersion(), "text/html", bytes, Code.OK);

	}

	public static void serverStatic(String filename, Socket socket, String version, MyCache cache) throws IOException {

		// out方法不一定会及时输出，err更方便debug，可以及时输出，常见场景：循环出错
		System.err.println(filename);

		// 获取文件类型
		String fileType = UrlUtils.getFileType(filename);

		byte[] fileContent = cache.checkCache(filename);
		// 如果有就调用缓存的
		if (fileContent == null) {

			fileContent = FileUtils.fileToByte(filename);
			// 如果上面有错就不会执行这里了
			cache.putIntoCache(filename, fileContent);

		}

		sendResponse(socket, version, fileType, fileContent, Code.OK);

	}

	public static void clientError(Socket socket, String version, String code, MyCache cache){

		// 向服务器记录报错信息
		System.err.println("bad gate!");

		// 错误页面直接缓存在内存里面的！
		sendResponse(socket, version, "text/html", cache.getErrorCache(code), code);

	}

	public static void sendResponse(Socket socket, String version, String fileType, byte[] fileContent, String code) {

		MyResponse res = new MyResponse(version, code, fileType, fileContent);

		res.sendResponse(socket);

	}

	public static Object[] setParam(Map<String, String> inputParamMap, MyMethod myMethod, Integer paraCount)
			throws ParamException {

		// 如果没有参数需求，就无需校正了
		if (inputParamMap == null) {
			return null;
		}

		// 准备：最终参数数组
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

			// 只提供几种常见的转型先？
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

		return paramArray;
	}
}

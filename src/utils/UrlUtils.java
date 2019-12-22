package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import bean.ParsedResult;
import config.ProxyConfig;
import exceptions.IllegalParamInputException;
import message.Message;
import message.ServerType;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class UrlUtils {

	/**
	 * 反正根据那个什么含蓄类原则，这种全是static的工具类要不能暴露new方法......
	 */
	private UrlUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * <hr>
	 * 匹配静态文件or动态方法请求， 如果是动态方法，同时记录uri参数
	 * <hr>
	 * 以下是几种合法的匹配例子：
	 * 
	 * <ul>
	 * <li>1.lerie/</li>
	 * <li>以/结尾，默认是lerie/index.html--->静态文件</li>
	 * </ul>
	 * 
	 * <ul>
	 * <li>2.lerie.html</li>
	 * <li>以.xxx结尾--->静态文件</li>
	 * </ul>
	 * 
	 * <ul>
	 * <li>3.lerie.html?abc=2</li>
	 * <li>以.xxx结尾并带有参数--->静态文件，忽略参数</li>
	 * </ul>
	 * 
	 * <ul>
	 * <li>4.lerie</li>
	 * <li>没有/，也没有.xxx--->动态方法且不带参数</li>
	 * </ul>
	 * 
	 * <ul>
	 * <li>5.lerie?name=var&says=hey</li>
	 * <li>有参数--->动态方法，带参数</li>
	 * </ul>
	 * 
	 * @param uri
	 * @return
	 * @throws IllegalParamInputException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static ParsedResult parseUri(String uri) throws IllegalParamInputException, FileNotFoundException, IOException {

		
		// 先做代理判断
		String proxyUri = ProxyConfig.getInstance().getProxy(uri);
		if (proxyUri!=null) {

			return new ParsedResult(proxyUri, ServerType.PROXY, null);
		}

		// 处理默认目录
		if ('/' == uri.charAt(uri.length() - 1)) {
			uri = uri + Message.DEFAULT_SUFFIX;
		}

		// 这是一段很迷惑的正则表达式.....
		String regex = "^/[-A-Za-z0-9/]+[.][A-Za-z]+[?]?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+?";
		// 判断是静态文件
		if (uri.matches(regex)) {

			String filename = Message.ROOT_PATH + uri;

			return new ParsedResult(filename, ServerType.STATIC_RESOURCES, null);
		}

		// 判断是动态文件
		else {

			HashMap<String, String> params = null;

			// 如果包含了有参数就处理参数？？？不行，改一下匹配规则
			if (uri.contains("?")) {
				// 获取参数
				String paramStr = uri.substring(uri.lastIndexOf("?") + 1);

				params = getParamMap(paramStr);

				// 然后变换字符串得到正确的uri
				uri = uri.substring(0, uri.lastIndexOf("?"));

			}

			return new ParsedResult(uri, ServerType.DYNAMIC_JAVA, params);

		}

	}

	/**
	 * 判断文件类型
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileType(String filename) {

		filename = filename.toLowerCase();

		if (filename.contains(".html")) {
			return "text/html";
		}
		if (filename.contains(".gif")) {
			return "image/gif";
		}
		if (filename.contains(".png")) {
			return "image/png";
		}
		if (filename.contains(".jpg")) {
			return "image/jpg";
		}
		if (filename.contains(".jpeg")) {
			return "image/jpeg";
		}
		if (filename.contains(".ico")) {
			return "image/x-icon";
		}
		if (filename.contains(".svg")) {
			return "image/svg+xml";
		}
		if (filename.contains(".css")) {
			return "text/css";
		}
		if (filename.contains(".js")) {
			return "application/javascript";
		} else {
			return "text/plain";
		}
	}

	/**
	 * 解析Url参数的
	 * @param paramStr
	 * @return
	 * @throws IllegalParamInputException
	 */
	public static HashMap<String, String> getParamMap(String paramStr) throws IllegalParamInputException {

		HashMap<String, String> paramsMap = new HashMap<>(16);

		try {
			String[] params = paramStr.split("&");

			for (String s : params) {
				String[] p = s.split("=");
				paramsMap.put(p[0], p[1]);

			}
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
			throw new IllegalParamInputException();
		}

		return paramsMap;

	}

}

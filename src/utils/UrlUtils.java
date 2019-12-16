package utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import message.Message;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class UrlUtils {

	/**
	 * 如果是静态文件就返回文件的目录filename 否则返回dynamic这个串
	 * 
	 * @param uri
	 * @return
	 */
	public static String parseUri(String uri) {
		// 处理默认目录
		if ('/' == uri.charAt(uri.length() - 1)) {
			uri = uri + Message.DEFAULT_SUFFIX;
		}

		// 处理静态文件
		if (uri.contains(".")) {

			String filename = Message.ROOT_PATH + uri;

			return filename;
		}

		if (!uri.contains(".")) {

			// 和C的情况有点不一样，先不管
			return Message.DYNAMIC;
		}

		return Message.DYNAMIC;
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
		} else {
			return "text/plain";
		}
	}

	@Deprecated
	public static Map<String, String> getParamMap(String uri) {
		
		//如果是没有参数的就是空的 返回 
		if(!uri.contains("?"))
		{
			return null;
		}
		
		String paramStr = uri.substring(uri.lastIndexOf("?") + 1);

		Map<String, String> paramsMap = new HashMap<String, String>();

		String[] params = paramStr.split("&");

		for (String s : params) {
			String[] p = s.split("=");
			paramsMap.put(p[0], p[1]);

		}

		return paramsMap;

	}
	
	
	public static String[] getParamArray(String uri) {
		
		//如果是没有参数的就是空的 返回 
		if(!uri.contains("?"))
		{
			return null;
		}
		
		String paramStr = uri.substring(uri.lastIndexOf("?") + 1);
		

		String[] params = paramStr.split("&");

		String[] results = new String[params.length];
				
		Integer i = 0;
		
		for (String s : params) {
			String[] p = s.split("=");
			results[i++] = p[1];

		}

		return results;

	}
	
	
	
}

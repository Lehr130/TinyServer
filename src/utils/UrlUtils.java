package utils;

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


}

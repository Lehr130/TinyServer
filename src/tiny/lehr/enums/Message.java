package tiny.lehr.enums;

import java.io.File;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public interface Message {
	
	String DYNAMIC = "dynamic";

	//使用webroot直接获取目录
	String ROOT_PATH = System.getProperty("user.dir")+File.separator+"webroot";
	
	String LOAD_PATH = Message.ROOT_PATH + File.separator + "webapps";
	
	String DEFAULT_SUFFIX = "index.html";
	
	String SLASH = "/";
	
	String HTML_SUFFIX = ".html";
	
	String GET = "GET";
	
	String POST = "POST";
	
	String DELETE = "DELETE";
	
	String PUT = "PUT";
	
	/**
	 * 我觉得每次就为了这一个东西而传参很麻烦所以就偷懒了
	 */
	String DEFAULT_HTTP_VERSION = "HTTP/1.1";
	
	
}

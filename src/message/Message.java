package message;


/**
 * @author Lehr
 * @date 2019年12月11日
 */
public interface Message {
	
	String DYNAMIC = "dynamic";

	String ROOT_PATH = "D://LehrsJavaEE/TinyServer/src/main" ;
	
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

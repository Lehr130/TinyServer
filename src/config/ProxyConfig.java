package config;

public class ProxyConfig {

	
	public static boolean isProxy(String uri)
	{
		return true;
	}
	
	public static String getProxyPath(String uri)
	{
		return "http://www.baidu.com"+uri;
	}
	
}

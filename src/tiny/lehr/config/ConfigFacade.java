package tiny.lehr.config;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigFacade {
	
	/**
	 * 自己是个单例模式，从而保证了配置类对象也是单例的，饿汉模式
	 */
	private static ConfigFacade instance = new ConfigFacade();
	
	private AttributeConfig attributeConfig;
	private ProxyConfig proxyConfig;
	
	/**
	 * 创建的时候自动载入各种配置文件
	 * @throws IOException
	 */
	private ConfigFacade()
	{
		//饿汉的话这样抛错好像就很麻烦了
		try {
			attributeConfig = new AttributeConfig();
			proxyConfig = new ProxyConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ConfigFacade getInstance()
	{
		return instance;
	}
	
	
	public String getProxy(String uri)
	{
		return proxyConfig.getProxy(uri);
	}
	
	public Integer getAttributeInteger(String attributeName)
	{
		return attributeConfig.getAttributeInteger(attributeName);
	}
	
	
	
}

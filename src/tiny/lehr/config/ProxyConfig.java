package tiny.lehr.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProxyConfig {

	private Map<String,String> proxyMap; 
	
	protected ProxyConfig() throws FileNotFoundException, IOException
	{
		proxyMap = new HashMap<>(16);
		//加载代理配置
		ConfigLoader.loadPorxyConfig(proxyMap);
	}


	/**
	 * 这里还需要修改
	 * @param uri
	 * @return
	 */
	protected String getProxy(String uri)
	{
		
		for(String regex : proxyMap.keySet())
		{
			if(uri.matches(regex))
			{
				System.err.println("成功代理！！！");
				return proxyMap.get(regex);
			}
		}
		
		return null;
		
	}
	
	
}

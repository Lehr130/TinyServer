package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import message.Message;

public class ProxyConfig {

	private Map<String,String> proxyMap; 
	
	protected ProxyConfig() throws FileNotFoundException, IOException
	{
		proxyMap = new HashMap<>(16);
		//加载代理配置
		loadingProxyPath();		
	}
	
	
	private void loadingProxyPath() throws FileNotFoundException, IOException
	{
		try (BufferedReader br = new BufferedReader( new FileReader(Message.ROOT_PATH+File.separator+"systemFile"+File.separator+"ProxyConfig.properties"))) 
		{
			String line = br.readLine();
			while (line != null) {
				line = line.trim();
				if (line.length() < 1 || line.charAt(0) == '#') {
					line = br.readLine();
					continue;
				}
				String[] parts = line.split("=");
				proxyMap.put(parts[0].trim(), parts[1].trim());
				line = br.readLine();
			}
		}
	}
	
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

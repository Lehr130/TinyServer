package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import message.Message;

/**
 * 这个就是配置文件的数据在内存里的位置 但是我觉得我这里设计得很烂欸
 * 
 * @author Lehr
 * @date 2019年12月18日
 * 
 */
public class AttributeConfig {


	private Map<String, String> configMap;

	/**
	 * 只准在本报内调用
	 * @throws IOException
	 */
	protected AttributeConfig() throws IOException
	{
		
		configMap = new HashMap<>(16);
		//加载元数据配置
		loadConfigIntoMap();
				
	}

	
	private void loadConfigIntoMap() throws IOException {
		try (BufferedReader br = new BufferedReader( new FileReader(Message.ROOT_PATH+File.separator+"systemFile"+File.separator+"ServerConfig.properties"))) 
		{
			String line = br.readLine();
			while (line != null) {
				line = line.trim();
				if (line.length() < 1 || line.charAt(0) == '#') {
					line = br.readLine();
					continue;
				}
				String[] parts = line.split("=");
				configMap.put(parts[0].trim(), parts[1].trim());
				line = br.readLine();
			}
		}
	}


	protected Integer getAttributeInteger(String attributeName)
	{
		return Integer.parseInt(configMap.get(attributeName));
	}


	
}

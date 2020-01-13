package tiny.lehr.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	 * 只准在本包内调用
	 * @throws IOException
	 */
	protected AttributeConfig() throws IOException
	{
		
		configMap = new HashMap<>(16);
		//加载元数据配置
		ConfigLoader.loadAttribute(configMap);
				
	}

	/**
	 * 获取数值属性
	 * 这里可以写个StrKit
	 * @param attributeName
	 * @return
	 */
	protected Integer getAttributeInteger(String attributeName)
	{
		return Integer.parseInt(configMap.get(attributeName));
	}


	
}

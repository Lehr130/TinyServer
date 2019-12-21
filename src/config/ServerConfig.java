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
public class ServerConfig {

	
	
	public static Integer SOCKET_BUFFER_LEN;
	public static Integer THREAD_POOL_SIZE;
	public static Integer LISTEN_PORT;
	public static Integer CACHE_SIZE;

	private static Map<String, String> configMap = new HashMap<>(16);

	static {

		try {
			loadConfigIntoMap();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		SOCKET_BUFFER_LEN = importIntegerParam("SOCKET_BUFFER_LEN");
		THREAD_POOL_SIZE = importIntegerParam("THREAD_POOL_SIZE");
		LISTEN_PORT = importIntegerParam("LISTEN_PORT");
		CACHE_SIZE = importIntegerParam("CACHE_SIZE");
		
	}

	private static void loadConfigIntoMap() throws IOException {
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

	private static Integer importIntegerParam(String name) {

		return Integer.parseInt(configMap.get(name));

	}

}

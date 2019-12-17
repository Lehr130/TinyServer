package server;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class Startup {
	
	/**
	 * 入口：
	 * 在你机子1234端口开启一个服务（以后单独剥个配置文件出来）
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		//开始服务
		new TinyServer().startUp();
		
		

	}

}

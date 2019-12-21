package server;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class Startup {

	/**
	 * 入口： 在你机子1234端口开启一个服务
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// 开始服务
		//ITiny t =  (ITiny)LogProxy.factory(new TinyServer());
		//t.startUp();
		new TinyServer().startUp();
		
	}

}

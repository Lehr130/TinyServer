package server;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class Startup {

	
	public static void main(String[] args) throws Exception {

		// 开始服务
		//ITiny t =  (ITiny)LogProxy.factory(new TinyServer());
		//t.startUp();
		
		new TinyServer().startUp();
		
	}

}

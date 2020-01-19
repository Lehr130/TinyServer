package tiny.lehr.connector;

import tiny.lehr.cache.CacheFacade;
import tiny.lehr.config.ConfigFacade;
import tiny.lehr.router.RouterFacade;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class Bootstrap {


	public static void main(String[] args) throws Exception {

		//先加载所有的配置文件
		ConfigFacade.getInstance();
		CacheFacade.getInstance();
		RouterFacade.getInstance();

		Long startTime = System.currentTimeMillis();

		HttpConnector tinyServer= new HttpConnector();

		tinyServer.startUp();

		//定时是无效的，因为一直阻塞在上一个线程

		Long endTime = System.currentTimeMillis();

		while(endTime-startTime<100)
		{
			System.err.println(endTime-startTime);
			endTime = System.currentTimeMillis();
		}

		tinyServer.shutDown();



	}

}

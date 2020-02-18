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

		HttpConnector tinyServer= new HttpConnector();

		tinyServer.startUp();



	}

}

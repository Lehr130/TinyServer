package tiny.lehr.container;

import tiny.lehr.bean.MyMethod;
import tiny.lehr.enums.RequestType;

public class ContainerFacade {

	private static ContainerFacade container = new ContainerFacade();
	
	private Loader loader;
	private Router router;
	
	private ContainerFacade()
	{
		loader = new Loader();
		router = new Router();
		
		try {
			loader.loadAll(router);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public MyMethod getMethod(String uri, RequestType requestType) {
		return router.getMethod(uri, requestType);
	}
	
	
	public static ContainerFacade getInstance()
	{
		return container;
	}
	
	
	
	
}

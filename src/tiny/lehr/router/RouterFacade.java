package tiny.lehr.router;

import tiny.lehr.bean.MyMethod;
import tiny.lehr.enums.RequestType;

public class RouterFacade {

	private static RouterFacade container = new RouterFacade();
	
	private DynamicJavaLoader loader;
	private DynamicJavaRouter router;
	
	private RouterFacade()
	{
		loader = new DynamicJavaLoader();
		router = new DynamicJavaRouter();
		
		try {
			loader.loadAll(router);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public MyMethod getMethod(String uri, RequestType requestType) {
		return router.getMethod(uri, requestType);
	}
	
	
	public static RouterFacade getInstance()
	{
		return container;
	}
	
	
	
	
}

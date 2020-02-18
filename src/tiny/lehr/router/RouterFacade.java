package tiny.lehr.router;

import tiny.lehr.bean.MyMethod;

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
	
	public MyMethod getMethod(String uri, String requestMethod) {
		return router.getMethod(uri, requestMethod);
	}
	
	
	public static RouterFacade getInstance()
	{
		return container;
	}
	
	
	
	
}

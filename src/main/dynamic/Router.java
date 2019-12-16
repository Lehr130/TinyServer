package main.dynamic;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.junit.Test;

import bean.MyMethod;
import message.RequestType;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
public class Router {

	/**
	 * 单例对象
	 */
	private static Router router;

	private HashMap<String, MyMethod> map = new HashMap<String, MyMethod>();

	
	private Router() throws Exception {
		Class<?> clz = Class.forName("main.dynamic.Users");
		
		Method met = clz.getMethod("sayHey",String.class,String.class);
		
		System.out.println(met);
		
		map.put("/users/sayHey", new MyMethod(RequestType.GET, met, clz));
		
	}
	

	public static Router getInstance() throws Exception {
		// 为了解决线安全的问题这个东西的初始化就放到主线程里去
		if (router == null) {
			router = new Router();

		}
		return router;
	}


	/**
	 * 获取方法，这里规定方法返回值必须是字符串或者是空的
	 * @param <T>
	 * @param uri
	 * @param requestType
	 * @return
	 */
	public <T> MyMethod getMethod(String uri, RequestType requestType) {
		MyMethod m = map.get(uri);

		if(m==null)
		{
			return null;
		}
		
		if (requestType != m.getRequestType()) {
			return null;
		}

		return m;

	}

}

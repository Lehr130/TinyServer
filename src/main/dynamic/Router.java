package main.dynamic;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

import annotation.LehrsMethod;
import annotation.ParamName;
import bean.MyMethod;
import message.RequestType;

/**
 * @author Lehr
 * @date 2019年12月11日
 */
@SuppressWarnings("rawtypes")
public class Router {

	/**
	 * 单例对象
	 */
	private static Router router;

	private HashMap<String, MyMethod> map = new HashMap<String, MyMethod>();

	private Router() throws Exception {
		//注册所有方法
		registerMethod();

	}

	
	public void registerMethod() throws ClassNotFoundException
	{
		
		// 直接把所有类加载进去？？？
		// 暂时只能把所有方法写道dynamic下面
		
		Class<?> cls = Class.forName("main.dynamic.Dynamic");

		
		
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(LehrsMethod.class)) {
				//获取注解
				LehrsMethod lm = method.getAnnotation(LehrsMethod.class);
				//注册
				System.out.println("注册方法："+method.getName()+",uri:" + lm.pathUri() + "，请求类型：" + lm.requestType());
				
				
				//用于记录参数名和参数类型的表
				HashMap<String,Class> paraMap = new HashMap<>(16);
				
				//记录参数
				for(Parameter p : method.getParameters())
				{
					ParamName pn = p.getAnnotation(ParamName.class);
					paraMap.put(pn.name(), p.getType());
					System.out.println("Parameter:"+pn.name()+", Class:"+p.getType());
				}
				
				//存入
				map.put(lm.pathUri(), new MyMethod(lm.requestType(), method, cls,paraMap));
				
			}
		}
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
	 * 
	 * @param <T>
	 * @param uri
	 * @param requestType
	 * @return
	 */
	public <T> MyMethod getMethod(String uri, RequestType requestType) {
		MyMethod m = map.get(uri);

		if (m == null) {
			return null;
		}

		if (requestType != m.getRequestType()) {
			return null;
		}

		return m;

	}

}

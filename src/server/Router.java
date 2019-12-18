package server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.HashMap;

import annotation.LehrsMethod;
import annotation.ParamName;
import bean.MyMethod;
import exceptions.ConflictMethodException;
import message.Message;
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

	/**
	 * 路由记录表本体：一个uri对应一个方法
	 */
	private HashMap<String, MyMethod> map = new HashMap<>(16);

	/**
	 * 构造方法，注册所有的方法
	 * 
	 * @throws ClassNotFoundException
	 * @throws ConflictMethodException
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private Router() throws ClassNotFoundException, ConflictMethodException, MalformedURLException, IOException {
		// 注册所有方法
		registerMethod();

	}

	/**
	 * 注册所有的方法<br>
	 * 目前只能注册Dynamic那个类里的<br>
	 * 原理是：把这个类下面所有的方法一个一个扫描看有没有注解
	 * 
	 * @throws ClassNotFoundException
	 * @throws ConflictMethodException
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public void registerMethod() throws ClassNotFoundException, ConflictMethodException, MalformedURLException, IOException {

		//从深入剖析Tomcat上抄的，等我再研究研究，试试看能不能把所有载入的类都自动加载了，另外现在dynamic类需要手动编译了
		URL[] urls = new URL[1];
		URLStreamHandler streamHandler = null;
		File classPath = new File(Message.ROOT_PATH + File.separator + "webapps");
		String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
		urls[0] = new URL(null, repository, streamHandler);
		URLClassLoader loader = new URLClassLoader(urls);

		Class cls = loader.loadClass("dynamic.Dynamic");
		System.out.println(cls);

		// 通过反射获取类名
//		Class<?> cls = Class.forName("dynamic.Dynamic");

		// 遍历所有方法，通过注解找出来已经注册了的方法
		for (Method method : cls.getDeclaredMethods()) {

			if (method.isAnnotationPresent(LehrsMethod.class)) {
				// 获取注解
				LehrsMethod lm = method.getAnnotation(LehrsMethod.class);
				// 注册
				System.out.println("注册方法：" + method.getName() + ",uri:" + lm.pathUri() + "，请求类型：" + lm.requestType());

				// 检测冲突 这里我先规定的是不准同请求方式同uri的方法重载....具体我有点想不起SpringMVC的是怎么处理的了
				MyMethod existMethod = map.get(lm.pathUri());
				if (existMethod != null && existMethod.getRequestType().equals(lm.requestType())) {
					throw new ConflictMethodException("方法重复了！");
				}

				// 用于记录参数名和参数类型的表
				HashMap<String, Class> paraMap = new HashMap<>(16);

				// 记录参数
				for (Parameter p : method.getParameters()) {
					ParamName pn = p.getAnnotation(ParamName.class);
					paraMap.put(pn.name(), p.getType());
					System.out.println("Parameter:" + pn.name() + ", Class:" + p.getType());
				}

				// 存入
				map.put(lm.pathUri(), new MyMethod(lm.requestType(), method, cls, paraMap));

			}
		}
	}

	/**
	 * 单例模式获取对象
	 * 
	 * @return
	 * @throws ConflictMethodException
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws Exception
	 */
	public static Router getInstance() throws ClassNotFoundException, ConflictMethodException, MalformedURLException, IOException {
		// 为了解决线安全的问题这个东西的初始化就放到主线程里去
		if (router == null) {
			router = new Router();

		}
		return router;
	}

	/**
	 * 获取方法，这里规定方法返回值必须是字符串或者是空的 以后如果要实现重载功能的话就再在这里扩展
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

package server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import annotation.LehrsMethod;
import annotation.ParamName;
import annotation.ThisGonnaBeADynamicMethod;
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
		loadApps();

	}

	private void loadApps()
			throws ClassNotFoundException, ConflictMethodException, MalformedURLException, IOException {

		// 遍历webapps文件夹下所有jar
		File appsFile = new File(Message.LOAD_PATH);

		String[] list = appsFile.list();
		for (String fileName : list) {

			if (fileName.endsWith(".jar")) {
				Set<Class<?>> classes = loadSingleJar(fileName);
				System.err.println(classes);
				for (Class clazz : classes) {
					registerMethod(clazz);
				}

			}

		}

	}

	private static Set<Class<?>> loadSingleJar(String jarsName) throws ClassNotFoundException, IOException {

		String JarsPath = Message.LOAD_PATH + File.separator + jarsName;

		//建立一个set来放入加载到的对象
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

		//找到jar文件
		JarFile jarFile = new JarFile(JarsPath);

		//定义一个类加载器，去这个目录的这个文件里加载这个文件
		ClassLoader loader = new URLClassLoader(new URL[] { new URL("file:" + JarsPath) });

		//迭代器
		Enumeration<JarEntry> es = jarFile.entries();

		//开始对载入的内容逐个检查
		while (es.hasMoreElements()) {

			//下一个元素
			JarEntry jarEntry = es.nextElement();

			//获取文件名
			String name = jarEntry.getName();

			
			//只解析class文件	（以后要扩展一下迭代解析jar文件）
			if (name != null && name.endsWith(".class")) {

				// 加载到JVM里去 （这里是转换成包名）	
				Class<?> c = loader.loadClass(name.replace("/", ".").substring(0, name.length() - 6));

				// 如果确实是这个类加载器新加载进来的原来没有的（主要是我计划让用户导入一个jar，怕又载入了我的标签）
				if (c.getClassLoader().equals(loader)) {
					
					//同时对路径前后的/自动修正
					
					if(c.getAnnotation(ThisGonnaBeADynamicMethod.class)!=null)
					{
						// 以后这里再加一个检测这个类是否要被作为动态方法的标签
						// 获取到一个新的用户的类：放入集合
						classes.add(c);						
					}
				}
			}
		}
		
		//返回加载到的东西
		return classes;
	}

	private void registerMethod(Class clazz) throws ConflictMethodException {

		// 遍历所有方法，通过注解找出来已经注册了的方法
		for (Method method : clazz.getDeclaredMethods()) {

			if (method.isAnnotationPresent(LehrsMethod.class)) {
				// 获取注解
				LehrsMethod lm = method.getAnnotation(LehrsMethod.class);
				// 注册
				String requestPathUri = fixUri(clazz,method);
				
				System.out.println("注册方法：" + method.getName() + ",uri:" + requestPathUri + "，请求类型：" + lm.requestType());

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
				map.put(requestPathUri, new MyMethod(lm.requestType(), method, clazz, paraMap));

			}
		}

	}

	public String fixUri(Class clazz, Method method)
	{
		LehrsMethod lm = method.getAnnotation(LehrsMethod.class);
		
		//我也不知道为什么非要强转
		ThisGonnaBeADynamicMethod tg = (ThisGonnaBeADynamicMethod) clazz.getAnnotation(ThisGonnaBeADynamicMethod.class);
		
		String fixed = tg.pathUri()+"/"+lm.pathUri();
		
		if(fixed.charAt(0)!='/')
		{
			fixed = "/" + fixed;
			
		}
		
		fixed = fixed.replaceAll("[/]+", "/");
		
		if(fixed.charAt(fixed.length()-1)=='/')
		{
			fixed = fixed.substring(0, fixed.length()-1);
		}

		return fixed;
		
		
		
		
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
	public static Router getInstance()
			throws ClassNotFoundException, ConflictMethodException, MalformedURLException, IOException {
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

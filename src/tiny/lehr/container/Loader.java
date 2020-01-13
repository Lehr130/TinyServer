package tiny.lehr.container;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import tiny.lehr.annotation.ThisGonnaBeADynamicMethod;
import tiny.lehr.enums.Message;

@SuppressWarnings("rawtypes")
public class Loader {

	protected Loader()
	{
		
	}
	
	protected void loadAll(Router router) throws Exception {

		// 遍历webapps文件夹下所有jar
		File appsFile = new File(Message.LOAD_PATH);

		String[] list = appsFile.list();
		for (String fileName : list) {

			if (fileName.endsWith(".jar")) {
				Set<Class<?>> classes = loadJar(fileName);
				System.err.println(classes);
				for (Class clazz : classes) {
					router.registerMethod(clazz);
				}
			}
		}
	}

	private static Set<Class<?>> loadJar(String jarsName) throws Exception {

		String JarsPath = Message.LOAD_PATH + File.separator + jarsName;

		// 建立一个set来放入加载到的对象
		Set<Class<?>> classes = new LinkedHashSet<>();

		try (

				// 找到jar文件
				JarFile jarFile = new JarFile(JarsPath);

				// 定义一个类加载器，去这个目录的这个文件里加载这个文件
				URLClassLoader loader = new URLClassLoader(new URL[] { new URL("file:" + JarsPath) });) {

			// 迭代器
			Enumeration<JarEntry> es = jarFile.entries();

			// 开始对载入的内容逐个检查
			while (es.hasMoreElements()) {

				// 下一个元素
				JarEntry jarEntry = es.nextElement();
				
				// 获取文件名
				String name = jarEntry.getName();
				
				//Jar中Jar  暂时无效，不知道Jar中Jar是在哪个目录里的
				if(name!=null && name.endsWith(".jar"))
				{
					loadJar(name);
				}
				
				// 只解析class文件 （以后要扩展一下迭代解析jar文件）
				if (name != null && name.endsWith(".class")) {

					// 加载到JVM里去 （这里是转换成包名）
					Class<?> c = loader.loadClass(name.replace("/", ".").substring(0, name.length() - 6));

					// 如果确实是这个类加载器新加载进来的原来没有的（主要是我计划让用户导入一个jar，怕又载入了我的标签）
					if (c.getClassLoader().equals(loader)) {

						// 同时对路径前后的/自动修正
						if (c.getAnnotation(ThisGonnaBeADynamicMethod.class) != null) {
							// 以后这里再加一个检测这个类是否要被作为动态方法的标签
							// 获取到一个新的用户的类：放入集合
							classes.add(c);
						}
					}
				}
			}
		}
		// 返回加载到的东西
		return classes;
	}
	





}

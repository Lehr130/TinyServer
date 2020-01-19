package tiny.lehr.tomcat;

import java.net.URLClassLoader;

/**
 * @author Lehr
 * @create: 2020-01-17
 * 一个封装好了的类加载器，用于加载Servlet到Wrapper里
 * Every Context will have an singleton entity to load all of the classes into its own path
 *
 */
public class TommyLoader extends ClassLoader{

    /**
     * 先这样，到时候再来划分加载关系
     */
    ClassLoader classLoader = this.getClass().getClassLoader();


    String rootPath;




    public Class<?> loadClass(String servletClass) throws ClassNotFoundException {
        System.out.println(servletClass);
        //这就是在一个写死了的项目下加载一个servlet的过程
        System.out.println(classLoader);
        return classLoader.loadClass("tiny.lehr.tomcat.booter.SayHeyServlet");

    }


    public void setClassLoader(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}

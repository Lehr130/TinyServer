package tiny.lehr.container;

import tiny.lehr.container.valve.SayHeyValve;
import tiny.lehr.enums.Message;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Lehr
 * @create: 2020-01-17
 * 一个封装好了的类加载器，用于加载Servlet到Wrapper里
 *
 */
public class TommyLoader {

    /**
     * 先这样，到时候再来划分加载关系
     */
    ClassLoader classLoader = this.getClass().getClassLoader();
    String rootPath;







    public Class<?> loadClass(String servletClass) throws ClassNotFoundException {
        System.out.println(servletClass);
        //这就是在一个写死了的项目下加载一个servlet的过程
        System.out.println(classLoader);
        return classLoader.loadClass("tiny.lehr.container.booter.SayHeyServlet");

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

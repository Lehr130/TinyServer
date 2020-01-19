package tiny.lehr.tomcat.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Lehr
 * @create: 2020-01-17
 * 一个封装好了的类加载器，用于加载Servlet到Wrapper里
 * Every Context will have an singleton entity to load all of the classes into its own path
 *
 */
public class TommyWebAppLoader extends URLClassLoader{

    //TODO : 类加载器实现唯一性

    public TommyWebAppLoader(URL[] urls) {
        super(urls);
    }

    /**
     * 重写一个自定义的类加载器，用来打破双亲委派模型，但是同时要确保能够调用到ext加载器来加载java.lang的内容
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        name = name.replace("/", ".").substring(0, name.length() - 6);

        return super.loadClass(name);
    }

}

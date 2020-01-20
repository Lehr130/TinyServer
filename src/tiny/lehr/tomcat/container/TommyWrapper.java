package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.loader.TommyWebAppLoader;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Lehr
 * @create 2020-01-16
 * Wrapper级容器的实现，代表了一个Servlet小程序
 * 里面包含了一个Servlet，且只被初始化一次
 */
public class TommyWrapper extends TommyContainer {

    /**
     * 维持的servlet的类名
     */
    private String servletClass;

    /**
     * 自己内部维持的servlet实例
     */
    private Servlet myServlet;


    /**
     * 构造方法:
     * 获取类名和加载器
     * 通过加载器来加载好一个servlet
     *
     * @param servletClass
     * @param loader
     */
    public TommyWrapper(String servletClass, TommyWebAppLoader loader) {
        this.servletClass = servletClass;
        allocate(loader);
    }

    /**
     * 利用类加载器实例化一个servlet
     * 并调用init方法
     * 全局有且只有一次
     *
     * @param loader
     */
    private void allocate(TommyWebAppLoader loader) {
        //加载Servlet，前提是只加载一次
        if (myServlet == null) {
            try {

                myServlet = (Servlet) loader.loadClass(servletClass).getDeclaredConstructor().newInstance();

                myServlet.init(null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 基础阀方法：调用servlet的service方法
     *
     * @param req
     * @param res
     */
    @Override
    protected void basicValveInvoke(ServletRequest req, ServletResponse res) {
        try {
            myServlet.service(req, res);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

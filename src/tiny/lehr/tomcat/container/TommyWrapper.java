package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.bean.TommyFilterFactory;
import tiny.lehr.tomcat.bean.TommyServletConfig;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Lehr
 * @create 2020-01-16
 * Wrapper级容器的实现，代表了一个Servlet小程序
 * 里面包含了一个Servlet，且只被初始化一次
 */
public class TommyWrapper extends TommyContainer {


    private TommyFilterFactory filterFactory;

    /**
     * 维持的servlet的类名
     */
    private TommyServletConfig servletConfig;

    /**
     * 自己内部维持的servlet实例
     */
    private Servlet myServlet;


    private String servletUrl;

    /**
     * 构造方法:
     * 获取类名和加载器
     * 通过加载器来加载好一个servlet
     *
     * @param servletConfig
     * @param loader
     */
    public TommyWrapper(TommyServletConfig servletConfig, TommyWebAppLoader loader, TommyFilterFactory filterFactory) {

        this.servletConfig = servletConfig;

        this.filterFactory = filterFactory;

        allocate(loader);
    }

    public Servlet getMyServlet() {
        return myServlet;
    }

    /**
     * 利用类加载器实例化一个servlet
     * 并调用init方法
     * 全局有且只有一次
     * <p>
     * 我看原版tomcat写这个allocate和load方法的目的是因为他把wrapperValve写成外部类了
     * 他用allocat来获取对应的servlet
     * 而我写成内部类就没必要了
     *
     * @param loader
     */
    private void allocate(TommyWebAppLoader loader) {
        //加载Servlet，前提是只加载一次
        if (myServlet == null) {
            try {

                myServlet = (Servlet) loader.loadClass(servletConfig.getServletClassName()).getDeclaredConstructor().newInstance();

                ServletConfig config = servletConfig;

                //关于init---->在GenericServlet里，init(servletConfig)和init()是这个关系：
                //前者里的动作分为两步：1.servletConfig赋值；2. 执行init()
                myServlet.init(config);

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

            //其实这里都是直接从缓存里去取了，我因为设计的原因没有把那个写好...
            FilterChain chain = filterFactory.getFilterChain(this);

            chain.doFilter(req, res);

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

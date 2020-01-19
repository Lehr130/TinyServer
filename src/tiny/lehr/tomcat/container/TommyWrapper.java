package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.TommyLoader;
import tiny.lehr.tomcat.TommyPipeline;
import tiny.lehr.tomcat.container.TommyContainer;
import tiny.lehr.tomcat.valve.TommyValve;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyWrapper extends TommyContainer {

    private String servletClass;

    private Servlet myServlet;

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }


    public TommyWrapper(String servletClass, TommyLoader loader) {
        this.servletClass = servletClass;
        setLoader(loader);
    }


    /**
     * Using classloader to init a servlet
     */
    @Override
    void beforeInvoke() {

        //dealing with servlet: if it's uninitialized then init it
        allocate();

    }

    public void allocate() {
        //加载Servlet，前提是只加载一次
        if (myServlet == null) {
            try {

                myServlet = (Servlet) getLoader().loadClass(servletClass).getDeclaredConstructor().newInstance();

                //but I'm not sure how to init this servlet in global range only once
                //TODO : How to make this Servlet init only once ?!
                myServlet.init(null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * just invoke the servlet itself
     *
     * @param req
     * @param res
     */
    @Override
    void basicValveInvoke(ServletRequest req, ServletResponse res) {
        try {
            myServlet.service(req, res);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

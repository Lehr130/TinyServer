package tiny.lehr.container;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyWrapper implements TommyContainer {

    private Servlet myServlet;

    private ClassLoader loader;

    /**
     * 暂时还不区分两种Request
     */

    private ServletRequest request;
    private ServletResponse response;

    /**
     * 自己维护了一组管道工作
     */
    private TommyPipeline pipeline;

    public void invoke(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        pipeline.invoke(req,res);
    }

    public Servlet loadServlet()
    {
        //加载Servlet，前提是只加载一次
        if(myServlet==null)
        {
            try {
                myServlet = (Servlet)loader.loadClass("servletName").newInstance();

                myServlet.init(null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return myServlet;
    }

    public void setBasic()
    {
        TommyValve basic = new TommyWrapperValve();
        //把自己的servlet放进去？
        pipeline.setBasicValve(basic);
    }

    /**
     * 传入相应的加载器
     */
    public void setLoader(){};

    /**
     * 写一个内部类，用来作为基础阀控制Servlet的内容
     */
    protected class TommyWrapperValve implements TommyValve{


        @Override
        public void invoke(ServletRequest req, ServletResponse res) {
            try {
                myServlet.service(req,res);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}

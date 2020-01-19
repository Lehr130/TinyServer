package tiny.lehr.container;

import tiny.lehr.bean.TommyRequest;
import tiny.lehr.bean.TommyResponse;
import tiny.lehr.container.valve.TommyValve;

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

    private String servletClass;

    private Servlet myServlet;

    private TommyLoader loader;


    /**
     * 自己维护了一组管道工作
     */
    private TommyPipeline pipeline = new TommyPipeline();

    public void setServletClass(String servletClass)
    {
        this.servletClass = servletClass;
    }

    public String getServletClass()
    {
        return this.servletClass;
    }

    public void addValve(TommyValve valve)
    {
        pipeline.addValve(valve);
    }


    public void invoke(TommyRequest req,TommyResponse res) throws ServletException, IOException {
        //顺序不是很好，需要调整一下
        loadServlet();
        setBasic();
        pipeline.invoke(req,res);
    }

    public Servlet loadServlet()
    {
        //加载Servlet，前提是只加载一次
        if(myServlet==null)
        {
            try {

                myServlet = (Servlet)loader.loadClass(servletClass).getDeclaredConstructor().newInstance();

                myServlet.init(null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finish loading! now It is :"+myServlet);

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
    public void setLoader(TommyLoader loader){
        this.loader = loader;
    }

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

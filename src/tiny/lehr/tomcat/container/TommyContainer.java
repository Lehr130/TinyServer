package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.loader.TommyWebAppLoader;
import tiny.lehr.tomcat.TommyPipeline;
import tiny.lehr.tomcat.valve.TommyValve;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Lehr
 * @create 2020-01-16
 * 模仿Tomcat的那个Container设计
 * But I change it as an abstract class
 * in order to make my program much more clean
 *
 * 是个标识性接口
 */
public abstract class TommyContainer {


    private TommyPipeline pipeline = new TommyPipeline();
    /**
     * make this container work
     * @param req
     * @param res
     */
    public void invoke(ServletRequest req, ServletResponse res)
    {
        addBasicValve();
        pipeline.invoke(req, res);
    }



    /**
     * add a new valve into the pipline
     * @param valve
     */
    public void addValve(TommyValve valve){
        pipeline.addValve(valve);
    }

    public void addBasicValve()
    {
        pipeline.setBasicValve(new BasicValve());
    }

    /**
     * mainly about how to select the child container and get all of the jobs into the basic valve
     */
    protected abstract void basicValveInvoke(ServletRequest req, ServletResponse res);

    /**
     * send this inner class into the pipline as the basic valve
     */
    class BasicValve implements TommyValve{

        @Override
        public void invoke(ServletRequest req, ServletResponse res) {
            basicValveInvoke(req, res);
        }
    }



}

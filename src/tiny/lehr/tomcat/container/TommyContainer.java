package tiny.lehr.tomcat.container;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.tomcat.TommyPipeline;
import tiny.lehr.tomcat.lifecircle.*;
import tiny.lehr.tomcat.logger.TommyLogger;
import tiny.lehr.tomcat.valve.TommyValve;

import java.util.List;


/**
 * @author Lehr
 * @create 2020-01-16
 * 模仿Tomcat的那个Container设计
 *
 * <p>
 * 是个标识性接口
 */
public abstract class TommyContainer implements TommyLifecycle {


    //一个监听器管理器？？？
    private TommyLifecycleSupport lifecycle = new TommyLifecycleSupport(this);

    public TommyLogger getLogger() {
        return logger;
    }

    public void setLogger(TommyLogger logger) {
        this.logger = logger;
    }

    private TommyPipeline pipeline;

    private TommyLogger logger;

    protected TommyLifecycleSupport getLifecycle() {
        return lifecycle;
    }

    /**
     *
     *
     * @param req
     * @param res
     */
    public void invoke(MyRequest req, MyResponse res) {
        addBasicValve();
        pipeline.invoke(req, res);
    }


    /**
     *
     *
     * @param valve
     */
    public void addValve(TommyValve valve) {
        pipeline.addValve(valve);
    }

    public void addBasicValve() {
        pipeline.setBasicValve(new BasicValve());
    }





    /**
     * 定义了基础阀的工作过程
     *
     * @param req
     * @param res
     */
    protected abstract void basicValveInvoke(MyRequest req, MyResponse res);

    /**
     *
     */
    class BasicValve implements TommyValve {

        @Override
        public void invoke(MyRequest req, MyResponse res) {
            basicValveInvoke(req, res);
        }
    }


    @Override
    public void addLifecycleListener(TommyLifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
    }


    @Override
    public List<TommyLifecycleListener> findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }

    @Override
    public void removeLifecycleListener(TommyLifecycleListener listener) {
        lifecycle.removeLifecycleListener(listener);
    }


    private Boolean started = false;


    //子类具体要做的
    protected abstract void doStart() throws Exception;

    protected void beforeStart(){

        if(this instanceof TommyWrapper)
        {
            addLifecycleListener(new TommyWrapperLifecycleListener());
        }
        if(this instanceof TommyContext)
        {
            addLifecycleListener(new TommyContextLifecycleListener());
        }
        if(this instanceof TommyHost)
        {
            addLifecycleListener(new TommyHostLifecycleListener());
        }
        if(this instanceof TommyEngine)
        {
            addLifecycleListener(new TommyEngineLifecycleListener());
        }

    }

    //因为接口不能用synchronized  所以要具体实现过来
    @Override
    public synchronized void start(){

        logger.debug("正在启动容器");

        if (started) {
            System.out.println("md都开始了还搞锤子");
            return;
        }

        //我设计这个是为了一开始好添加监听器的
        beforeStart();

        pipeline = new TommyPipeline();

        lifecycle.fireLifecycleEvent(BEFORE_START_EVENT,null);

        started = true;

        //我这里又抽象了一次哈
        try {
            doStart();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //启动阀门
        pipeline.start();

        //通知所有监听器
        lifecycle.fireLifecycleEvent(START_EVENT, null);


        lifecycle.fireLifecycleEvent(AFTER_START_EVENT, null);

        logger.debug("容器启动完成");
    }


    protected abstract void doStop();


    @Override
    public synchronized void stop(){

        logger.debug("正在停止容器");

        if (!started) {
            System.out.println("md都结束了还搞锤子");
        }



        lifecycle.fireLifecycleEvent(BEFORE_STOP_EVENT,null);

        lifecycle.fireLifecycleEvent(STOP_EVENT,null);

        started = false;

        pipeline.stop();

        //停止子容器  但是我不知道为什么之而立是先stop event再去停止
        doStop();

        //TODO: 这里还有报错需要改进

        lifecycle.fireLifecycleEvent(AFTER_STOP_EVENT, null);
        logger.debug("容器停止完成");
    }


    public Boolean isStarted()
    {
        return  started;
    }

}

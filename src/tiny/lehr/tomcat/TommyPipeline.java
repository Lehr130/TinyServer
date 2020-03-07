package tiny.lehr.tomcat;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.tomcat.lifecircle.TommyLifecycle;
import tiny.lehr.tomcat.lifecircle.TommyLifecycleListener;
import tiny.lehr.tomcat.valve.TommyValve;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Lehr
 * @create 2020-01-16
 * 这个就是里面的管道流水线工程
 */
public class TommyPipeline implements TommyLifecycle {

    /**
     * 内部维护了一组阀门
     * 源码是用数组配一个内部类ValveContext控制来实现的
     * 然而我并不知道他为什么要这样做，所以我就选择直接List了
     */
    List<TommyValve> valveList = new ArrayList<>();

    /**
     * 基础阀任务，就是这个容器的核心任务
     */
    TommyValve basicValve;

    /**
     * 向管道里添加一个阀
     * @param valve
     */
    public void addValve(TommyValve valve)
    {
        valveList.add(valve);
    }

    /**
     * 启动管道任务：先启动每个普通阀，再启动基础阀
     * @param req
     * @param res
     */
    public void invoke(MyRequest req, MyResponse res)
    {
        //逐个invoke
        valveList.forEach(v->v.invoke(req,res));
        //执行基础阀
        basicValve.invoke(req,res);
    }

    /**
     * 设置基础阀
     * @param basic
     */
    public void setBasicValve(TommyValve basic)
    {
        this.basicValve = basic;
    }

    //TODO:!!!!
    @Override
    public void addLifecycleListener(TommyLifecycleListener listener) {

    }

    @Override
    public List<TommyLifecycleListener> findLifecycleListeners() {
        return null;
    }

    @Override
    public void removeLifecycleListener(TommyLifecycleListener listener) {

    }

    @Override
    public void start(){

    }

    @Override
    public void stop(){

    }
}

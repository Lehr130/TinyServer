package tiny.lehr.container;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create 2020-01-16
 * 这个就是里面的管道流水线工程
 */
public class TommyPipeline {

    /**
     * 内部维护了一组阀门
     * 源码是用数组配一个内部类ValveContext控制来实现的
     * 然而我并不知道他为什么要这样做，所以我就选择直接List了
     */
    List<TommyValve> valveList = new ArrayList<>();

    TommyValve basicValve;

    public void addValve(TommyValve valve)
    {
        valveList.add(valve);
    }

    public void invoke(ServletRequest req, ServletResponse res)
    {
        //逐个invoke
        valveList.forEach(v->v.invoke(req,res));
        //执行基础阀
        basicValve.invoke(req,res);
    }

    public void setBasicValve(TommyValve basic)
    {
        this.basicValve = basic;
    }



}

package tiny.lehr.tomcat.container;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyEngine extends TommyContainer {

    Map<String, TommyHost> hostMap = new HashMap<>();

    public TommyEngine() {}

    public void addHost(String hostname, String hostPath) throws Exception {
        hostMap.put(hostname, new TommyHost(hostname, hostPath));
    }


    public TommyHost getHost(MyRequest req) {
        String url = req.getHost();

        //获取
        TommyHost host = hostMap.get(url);

        return host;

    }

    @Override
    protected void basicValveInvoke(MyRequest req, MyResponse res) {

        TommyHost host = getHost(req);
        host.invoke(req, res);

    }


    @Override
    protected void doStart() {
        hostMap.values().forEach(TommyHost::start);
    }

    @Override
    protected void doStop() {

    }
}

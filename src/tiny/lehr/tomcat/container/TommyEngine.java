package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.bean.TommyHttpRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyEngine extends TommyContainer {

    Map<String, TommyHost> hostMap;

    public TommyEngine() {
        hostMap = new HashMap<>();
    }

    public void addHost(String hostname, String hostPath) throws Exception {
        hostMap.put(hostname, new TommyHost(hostname, hostPath));
    }


    public TommyHost getHost(ServletRequest req) {
        String url = ((TommyHttpRequest) req).getE();

        //检查之前实例化过这个wrapper没有
        Boolean alreadyHave = hostMap.containsKey(url);

        //如果没有就加载，不然直接返回


        //获取
        TommyHost host = hostMap.get(url);

        return host;

    }

    @Override
    protected void basicValveInvoke(ServletRequest req, ServletResponse res) {

        TommyHost host = getHost(req);
        host.invoke(req, res);

    }
}

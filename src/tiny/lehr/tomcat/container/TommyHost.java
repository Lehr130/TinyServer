package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.bean.TommyHttpRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 * TODO 解压war包
 * TODO 实现热部署
 */
public class TommyHost extends TommyContainer {

    private String hostname;

    private String webPath;

    private Map<String, TommyContext> contextMap;

    public TommyHost(String hostname,String webPath) throws Exception {
        this.hostname = hostname;
        this.webPath = webPath;
        contextMap = new HashMap<>();
        getWebapps();
    }


    private void getWebapps() throws Exception {

        File webRoot = new File(webPath);
        File[] apps = webRoot.listFiles();
        for(File f: apps)
        {
            String name = f.getName();
            TommyContext context = new TommyContext(webPath+File.separator+name);
            contextMap.put(name,context);
        }


    }

    private TommyContext getContext(ServletRequest req) {

        String url = ((TommyHttpRequest)req).getC();

        TommyContext context= contextMap.get(url);

        return context;
    }

    @Override
    protected void basicValveInvoke(ServletRequest req, ServletResponse res) {

        TommyContext context = getContext(req);

        context.invoke(req,res);

    }
}

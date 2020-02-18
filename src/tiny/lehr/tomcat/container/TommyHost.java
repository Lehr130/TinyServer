package tiny.lehr.tomcat.container;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;

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
            //IMPROVE: 关于context映射的一个不好的设计
            contextMap.put("/"+name,context);
        }


    }

    private TommyContext getContext(MyRequest req) {

        String url = req.getContextPath();

        TommyContext context= contextMap.get(url);

        return context;
    }

    @Override
    protected void basicValveInvoke(MyRequest req, MyResponse res) {

        TommyContext context = getContext(req);

        context.invoke(req,res);

    }
}

package tiny.lehr.tomcat;

import tiny.lehr.tomcat.container.TommyWrapper;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-19
 */
public class TommyMapper {

    /**
     * mapping the url and the servlet quality name so that loader can init a wrapper with url
     */
    private Map<String, String> wrapperMap = new HashMap<>(16);


    public void addWrapper(String url, String servletName) {

        wrapperMap.put(url,servletName);

    }



    public TommyWrapper getWrapper(ServletRequest req, TommyWebAppLoader loader) {

        //TODO : 实现通过url来找到Servlet类名的接口

        //the biggest question is how to get url from the servlet Request

        //get the qualified name of this servlet

        String servletClass = wrapperMap.get("something");

        //using loader to init a wrapper
        TommyWrapper wrapper = new TommyWrapper(servletClass,loader);

        //then send it back
        return wrapper;

    }


}

package tiny.lehr.tomcat.bean;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-22
 * 代表了从web.xml里读取到的一个元素信息
 */
public class TommyServletDef {


    private String servletName;
    private String servletClassName;
    private String servletUrl;
    private Map<String, String> initParameters;
    private ServletContext servletContext;


    public TommyServletDef(String servletName, String servletClassName, Map<String, String> initParameters) {
        this.servletName = servletName;
        this.servletClassName = servletClassName;
        this.initParameters = initParameters;
    }


    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public void setServletClassName(String servletClassName) {
        this.servletClassName = servletClassName;
    }

    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }


    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getServletUrl() {
        return servletUrl;
    }

    public String getServletClassName() {
        return servletClassName;
    }


    public void setServletUrl(String servletUrl) {
        this.servletUrl = servletUrl;
    }


    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


}

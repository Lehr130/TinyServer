package tiny.lehr.tomcat.bean;

import tiny.lehr.utils.EnumerationUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-22
 */
public class TommyServletConfig implements ServletConfig {


    private String servletName;
    private String servletClassName;
    private String servletUrl;
    private Map<String, String> initParameters;
    private ServletContext servletContext;


    public TommyServletConfig(String servletName, String servletClassName, Map<String, String> initParameters) {
        this.servletName = servletName;
        this.servletClassName = servletClassName;
        this.initParameters = initParameters;
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

    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String s) {
        return initParameters.get(s);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {

        return EnumerationUtils.getEnumerationStringByMap(initParameters);

    }
}

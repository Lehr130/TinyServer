package tiny.lehr.tomcat.bean;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-23
 */
public class TommyFilterConfig implements FilterConfig {

    private Integer initNumber;
    private String filterName;
    private String filterClassName;
    private String filterUrl;
    private Map<String,String> initParameters;
    private ServletContext servletContext;

    public TommyFilterConfig(String filterName, String filterClass, Map<String, String> parameters) {
        this.filterName = filterName;
        this.filterClassName = filterClass;
        this.initParameters = parameters;
    }


    public Integer getInitNumber() {
        return initNumber;
    }

    public void setInitNumber(Integer initNumber) {
        this.initNumber = initNumber;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterClassName() {
        return filterClassName;
    }

    public void setFilterClassName(String filterClassName) {
        this.filterClassName = filterClassName;
    }

    public String getFilterUrl() {
        return filterUrl;
    }

    public void setFilterUrl(String filterUrl) {
        this.filterUrl = filterUrl;
    }

    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String getFilterName() {
        return null;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public String getInitParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }
}

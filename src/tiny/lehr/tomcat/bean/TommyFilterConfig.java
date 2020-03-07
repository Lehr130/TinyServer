package tiny.lehr.tomcat.bean;

import tiny.lehr.utils.EnumerationUtils;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-23
 * 有点类似于原版里的ApplicatFilterConfig
 * 代表了一个Filter的各项配置情况
 */
public class TommyFilterConfig implements FilterConfig {

    private Filter filter;

    /**
     * 名字
     */
    private String filterName;

    /**
     * 类名
     */
    private String filterClassName;

    /**
     * 要过滤的url
     * 以后应该要换成数组之类的
     */
    private String filterUrl;

    /**
     * 各项初始参数
     */
    private Map<String, String> initParameters;

    /**
     * 代表webapp的上下文内容
     */
    private ServletContext servletContext;


    public TommyFilterConfig(String filterName, String filterClass, Map<String, String> parameters) {
        this.filterName = filterName;
        this.filterClassName = filterClass;
        this.initParameters = parameters;
    }


    public String getFilterClassName() {
        return filterClassName;
    }


    public String getFilterUrl() {
        return filterUrl;
    }

    public void setFilterUrl(String filterUrl) {
        this.filterUrl = filterUrl;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 获取配置时候的过滤器的名字
     *
     * @return
     */
    @Override
    public String getFilterName() {
        return filterName;
    }

    /**
     * 获取上下文配置
     *
     * @return
     */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * 通过名字获取某个参数
     *
     * @param s
     * @return
     */
    @Override
    public String getInitParameter(String s) {
        return initParameters.get(s);
    }

    /**
     * 获取参数名字的集合
     *
     * @return
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        return EnumerationUtils.getEnumerationStringByMap(initParameters);
    }

    public void setFilter(Filter myFilter) {
        filter = myFilter;
    }

    public Filter getFilter() {
        return filter;
    }

    public void destory(){
        filter.destroy();
    }
}

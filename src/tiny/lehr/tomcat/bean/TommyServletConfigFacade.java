package tiny.lehr.tomcat.bean;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * @author Lehr
 * @create: 2020-02-18
 * 一个门面对象
 */
public class TommyServletConfigFacade implements ServletConfig {

    private ServletConfig sc;

    public TommyServletConfigFacade(ServletConfig sc)
    {
        this.sc = sc;
    }

    @Override
    public String getServletName() {
        return sc.getServletName();
    }

    @Override
    public ServletContext getServletContext() {
        return sc.getServletContext();
    }

    @Override
    public String getInitParameter(String s) {
        return sc.getInitParameter(s);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return sc.getInitParameterNames();
    }
}

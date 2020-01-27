package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.bean.TommyFilterChain;
import tiny.lehr.tomcat.bean.TommyFilterConfig;
import tiny.lehr.tomcat.bean.TommyHttpRequest;
import tiny.lehr.tomcat.bean.TommyServletDef;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;
import tiny.lehr.utils.EnumerationUtils;
import tiny.lehr.utils.UrlUtils;

import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 * Wrapper级容器的实现，代表了一个Servlet小程序
 * 里面包含了一个Servlet，且只被初始化一次
 */
public class TommyWrapper extends TommyContainer implements ServletConfig {

    private TommyContainer parent;

    private String servletName;

    private String servletClassName;

    private Map<String, String> initParameters;

    private ServletContext servletContext;

    private Servlet myServlet;

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

    public void setParent(TommyContainer parent) {

        if ((parent != null) && !(parent instanceof TommyContext)) {
            System.out.println("出错了");
        }
        this.parent = parent;
    }


    public TommyWrapper(TommyContext parent, TommyServletDef config) {

        setParent(parent);

        servletName = config.getServletName();
        servletClassName = config.getServletClassName();
        initParameters = config.getInitParameters();
        servletContext = ((TommyContext) parent).getServletContext();

    }


    private void allocate(TommyWebAppLoader loader) {
        //加载Servlet，前提是只加载一次
        if (myServlet == null) {
            try {

                myServlet = (Servlet) loader.loadClass(servletClassName).getDeclaredConstructor().newInstance();

                //TODO 到时候记得用Facade处理一下
                myServlet.init(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void basicValveInvoke(ServletRequest req, ServletResponse res) {
        try {

            allocate(((TommyContext) parent).getLoader());
            TommyFilterChain filterChain = createFilterChain(parent, req);

            filterChain.doFilter(req, res);

            filterChain.release();

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public TommyFilterChain createFilterChain(TommyContainer parent, ServletRequest req) {

        Map<String, TommyFilterConfig> filterPool = ((TommyContext) parent).getFilterPool();
        String servletUrl = ((TommyHttpRequest) req).getServletUrl();


        TommyFilterChain chain = new TommyFilterChain();
        chain.setServlet(myServlet);


        filterPool.forEach((name, filterConfig) ->
        {
            //TODO 如果url满足要求就加入chain 以后写个判定算法
            Boolean flag = UrlUtils.isMatch(servletUrl, filterConfig.getFilterUrl());
            if (flag) {

                chain.addFilter(filterConfig.getFilter());
            }

            //TODO 如果servlet-name满足要求也加入（虽然现在还没实现这个）
            if (false) {
                chain.addFilter(filterConfig.getFilter());
            }

        });

        return chain;
    }

}

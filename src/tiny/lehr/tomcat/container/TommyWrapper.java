package tiny.lehr.tomcat.container;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.tomcat.bean.*;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;
import tiny.lehr.utils.EnumerationUtils;
import tiny.lehr.utils.UrlUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
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

    //其实我觉得这里就别用接口了诶
    private TommyContext parent;

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


    public TommyWrapper(TommyContext parent, TommyServletDef config) {

        this.parent = parent;
        servletContext = parent.getServletContext();
        servletName = config.getServletName();
        servletClassName = config.getServletClassName();
        //获取servlet的初始参数 可在servletConfig里获得
        initParameters = config.getInitParameters();

    }


    private void allocate() {
        //加载Servlet，前提是只加载一次
        synchronized (this) {
            if (myServlet == null) {
                try {

                    //监听器记录

                    getLifecycle().fireLifecycleEvent(BEFORE_INIT_EVENT, myServlet);
                    loadServlet();
                    getLifecycle().fireLifecycleEvent(AFTER_INIT_EVENT, myServlet);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void loadServlet() throws Exception {

        TommyWebAppLoader loader = parent.getLoader();

        myServlet = (Servlet) loader.loadClass(servletClassName).getDeclaredConstructor().newInstance();

        //门面对象包一下
        myServlet.init(new TommyServletConfigFacade(this));


    }

    @Override
    protected void basicValveInvoke(MyRequest req, MyResponse res) {

        try {

            //分配servlet  只有第一次才会去实例化
            allocate();

            TommyFilterChain filterChain = createFilterChain(parent, req);

            //门面对象放进去！
            filterChain.doFilter(new TommyRequestFacade(req), new TommyResponseFacade(res));

            //一个不优雅的设计：如果是首次有session的话就要放到res里
            //JSESSIONID=ED3BB12BCEBB7F0467ED901C82FF5833; Path=/TommyTest_war_exploded; HttpOnly
            //后面那些是啥我就不知道了？？？
            //TODO: URL 重写？？？
            //这时候就需要返回sessionId了
            HttpSession session = req.getSession(false);
            if (session != null && session.isNew()) {
                res.addCookie(new Cookie("JSESSIONID", session.getId()));
            }

            //释放filters
            filterChain.release();

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }


    }

    public TommyFilterChain createFilterChain(TommyContext parent, MyRequest req) {

        Map<String, TommyFilterConfig> filterPool = parent.getFilterPool();
        String servletUrl = req.getServletPath();


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


    @Override
    protected void doStart() {
        System.out.println("Wrapper is starting...");
    }

    @Override
    protected void doStop() {

        System.out.println("Wrapper is stopping...");

        //销毁servlet
        if(myServlet!=null)
        {
            myServlet.destroy();
        }
    }


}

package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.TommyContextConfig;
import tiny.lehr.tomcat.bean.TommyFilterConfig;
import tiny.lehr.tomcat.bean.TommyHttpRequest;
import tiny.lehr.tomcat.bean.TommyServletContext;
import tiny.lehr.tomcat.bean.TommyServletDef;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;
import tiny.lehr.utils.UrlUtils;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 * Context级容器的实现，代表了一个项目的级别，即war的容器
 * 里面包含了多个Wrapper级子容器，用来管理
 */
public class TommyContext extends TommyContainer {

    private String appName;

    private TommyWebAppLoader loader;

    private String appPath;

    private Map<String, TommyWrapper> wrappers;

    private TommyServletContext servletContext;

    private Boolean avaliavle = false;

    private TommyContextConfig context;

    private Map<String, TommyFilterConfig> filterPool;

    public TommyContext(String appPath) throws Exception {

        //获取路径
        this.appPath = appPath;

        //建立好存放子容器的哈希表
        wrappers = new HashMap<>();

        //通过路径准备好类加载器
        loader = new TommyWebAppLoader(appPath);

        //解析web.xml内容
        try {
            context = new TommyContextConfig(appPath);
            avaliavle = true;
        } catch (Exception e) {
            System.out.println("炸了！");
        }

        if (avaliavle == false) {
            System.exit(1);
        }


        //获取域对象
        servletContext = new TommyServletContext(context.getContextInitParameters(), appPath);

        initAllFilter(context.getFilterConfigMap());

    }

    private void initAllFilter(Map<String, TommyFilterConfig> filterConfigMap) {

        filterPool = new HashMap<>();

        filterConfigMap.forEach((filterName, filterConfig) -> {
            try {

                filterConfig.setServletContext(servletContext);

                Filter myFilter = (Filter) loader.loadClass(filterConfig.getFilterClassName()).getDeclaredConstructor().newInstance();

                myFilter.init(filterConfig);

                filterConfig.setFilter(myFilter);

                filterPool.put(filterName, filterConfig);

            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    /**
     * 通过servlet请求获取子容器
     * 请求化为servletUrl--->如果之前加载了，就直接返回实例，如果没有，就加载后放入池中然后返回
     *
     * @param req
     * @return
     */
    private TommyWrapper getWrapper(ServletRequest req) {

        String servletUrl = ((TommyHttpRequest) req).getServletUrl();

        //检查之前实例化过这个wrapper没有
        Boolean alreadyHave = wrappers.containsKey(servletUrl);

        //如果没有就加载，不然直接返回
        if (alreadyHave == false) {
            addWrapper(servletUrl);
        }

        //获取
        TommyWrapper wrapper = wrappers.get(servletUrl);

        return wrapper;

    }


    public Map<String, TommyFilterConfig> getFilterPool() {
        return filterPool;
    }

    public TommyServletContext getServletContext() {
        return servletContext;
    }

    /**
     * 把一个没有实例化过的servlet通过类加载器实例化
     * 并放入到wrappers里去
     *
     * @param servletUrl
     */
    private void addWrapper(String servletUrl) {

        TommyServletDef servletDef = null;
        //通过类名，借助类加载器来实例化一个管理本Servlet的Wrapper
        Map<String, TommyServletDef> servletDefMap = context.getServletDefMap();

        for(Map.Entry<String,TommyServletDef> e: servletDefMap.entrySet())
        {
            if(UrlUtils.isMatch(servletUrl,e.getValue().getServletUrl()))
            {
                servletDef = e.getValue();
                break;
            }
        }

        //这说明用户乱输入没有的了
        if(servletDef==null)
        {
            System.out.println("出大问题了！！！！！");
        }

        TommyWrapper wrapper = new TommyWrapper(this,servletDef);

        //加入到wrappers池里去
        wrappers.put(servletUrl, wrapper);
    }

    public TommyWebAppLoader getLoader() {
        return loader;
    }

    /**
     * 一个小知识点：重写的时候权限范围不能比父类更小
     * 设置基础阀任务：选择正确的Wrapper并启动
     *
     * @param req
     * @param res
     */
    @Override
    protected void basicValveInvoke(ServletRequest req, ServletResponse res) {

        //找到正确的wrapper
        TommyWrapper wrapper = getWrapper(req);

        //启动wrapper
        try {
            wrapper.invoke(req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

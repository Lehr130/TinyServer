package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.TommyMapper;
import tiny.lehr.tomcat.TommyXmlParser;
import tiny.lehr.tomcat.bean.TommyFilterFactory;
import tiny.lehr.tomcat.bean.TommyHttpRequest;
import tiny.lehr.tomcat.bean.TommyServletConfig;
import tiny.lehr.tomcat.bean.TommyServletContext;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;

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


    /**
     * 这个是容器自带的载入器，用来加载对应的这个应用的目录文件夹下的类
     * <p>
     * TODO: 目前双亲委派的问题还没有解决
     */
    private TommyWebAppLoader loader;


    /**
     * 这个是本应用对应的一个servlet路径映射管理器
     * 通过解析web.xml来获得映射关系
     * 并提供通过请求url来查询对应的servlet的类名的功能
     */
    private TommyMapper mapper;


    /**
     * 这个是本应用对应的路径，即，从主目录上加上自己的项目名
     */
    private String appPath;


    /**
     * 这个是所有的子容器Wrapper的一个存放池
     * 按需加载
     * 第二次之后直接查找存在的
     * 给入servlet对应的url，返回对应的wrapper
     * 从而保证了每个servlet只会被初始化一次
     */
    private Map<String, TommyWrapper> wrappers;


    private TommyServletContext servletContext;

    /**
     * 这个其实更类似一个上下文Context
     * 但是servlet-mapping多个的情况还是处理不了
     */
    private TommyXmlParser parser;

    private TommyFilterFactory filterFactory;

    /**
     * 原版是有个FilterDef和FilterMap的类来分开解析两个标签的
     * 我懒，我写到一起了，都叫FilterConfig
     */

    /**
     * 构造的过程：
     * 获得容器路径--->建立好存放子容器的哈希表--->通过路径准备好类加载器--->通过路径准备好映射器
     *
     * @param appPath
     */
    public TommyContext(String appPath) throws Exception {

        //获取路径
        this.appPath = appPath;

        //建立好存放子容器的哈希表
        wrappers = new HashMap<>();

        //通过路径准备好类加载器
        loader = new TommyWebAppLoader(appPath);


        parser = new TommyXmlParser(appPath);

        servletContext = new TommyServletContext(parser.getContextInitParameters(), appPath);

        //找到web.xml文件并交给mapper来处理
        mapper = new TommyMapper(parser,servletContext);

        filterFactory = new TommyFilterFactory(parser.getFilterConfigMap(),loader,servletContext);


    }


    /**
     * 通过servlet请求获取子容器
     * 请求化为servletUrl--->如果之前加载了，就直接返回实例，如果没有，就加载后放入池中然后返回
     *
     * @param req
     * @return
     */
    private TommyWrapper getWrapper(ServletRequest req) {

        //TODO : 实现通过ServletReq获得url 这里先还是mock
        String servletUrl = ((TommyHttpRequest) req).getN();

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


    /**
     * 把一个没有实例化过的servlet通过类加载器实例化
     * 并放入到wrappers里去
     *
     * @param servletUrl
     */
    private void addWrapper(String servletUrl) {
        //通过url获取类名
        TommyServletConfig servletConfig = mapper.getServletClass(servletUrl);

        //通过类名，借助类加载器来实例化一个管理本Servlet的Wrapper
        TommyWrapper wrapper = new TommyWrapper(servletConfig, loader,filterFactory);

        filterFactory.createFilterChain(servletUrl,wrapper);

        //加入到wrappers池里去
        wrappers.put(servletUrl, wrapper);
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

        // 启动wrapper
        try {
            wrapper.invoke(req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

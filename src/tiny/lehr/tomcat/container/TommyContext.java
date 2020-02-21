package tiny.lehr.tomcat.container;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.tomcat.TommyContextConfig;
import tiny.lehr.tomcat.TommySessionManager;
import tiny.lehr.tomcat.bean.TommyFilterConfig;
import tiny.lehr.tomcat.bean.TommyServletContext;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 * Context级容器的实现，代表了一个项目的级别，即war的容器
 * 里面包含了多个Wrapper级子容器，用来管理
 * <p>
 * FIXME: Wrapper不是按需加载，而是里面的servlet是按需实例化
 */
public class TommyContext extends TommyContainer {

    private String appName;

    private TommyWebAppLoader loader;

    private String appPath;


    //用url来找wrapper
    //IMPROVE: 感觉源码好像是 先根据配置文件实例化所有，但是只不过servlet没有实例化而已，然后他就好实现loadupstart那个顺序了
    private Map<String, TommyWrapper> wrappers;

    private TommyServletContext servletContext;

    private TommyContextConfig context;

    private Map<String, TommyFilterConfig> filterPool;

    private TommySessionManager sessionManager;

    private Boolean isPaused = true;


    public TommyContext(String name, String appPath) throws Exception {

        this.appName = name;
        //获取路径
        this.appPath = appPath;

    }

    public String getAppName() {
        return appName;
    }

    public TommySessionManager getSessionManager() {
        return sessionManager;
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
    private TommyWrapper getWrapper(MyRequest req) {

        String servletUrl = req.getServletPath();

        //检查之前实例化过这个wrapper没有
        Boolean alreadyHave = wrappers.containsKey(servletUrl);

        //如果没有就加载，不然直接返回
        if (alreadyHave == false) {
            System.out.println("没有！！！");
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


    private void loadWrappers() {
        wrappers = new HashMap<>();

        context.getServletDefMap().values().forEach(servletDef -> {
            TommyWrapper wrapper = new TommyWrapper(this, servletDef);
            //加入到wrappers池里去
            wrappers.put(servletDef.getServletUrl(), wrapper);
        });
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
    protected void basicValveInvoke(MyRequest req, MyResponse res) {


        //检查载入情况，以防重载的时候被调用
        while (isPaused) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }

        //源代码基本也是这样做的，只不过他是放在wrapper的基础阀里面
        //但，其实我觉得嘛，放这里也没啥
        req.setContext(this);

        //找到正确的wrapper
        TommyWrapper wrapper = getWrapper(req);

        //启动wrapper
        try {
            wrapper.invoke(req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void doStart() throws Exception {

        //FIXME：这里还有个设计：如果报错了的话，有一个叫做available的布尔值会为false然后本容器是无法调用的


        isPaused = true;

        //准备好session管理器
        sessionManager = new TommySessionManager(this);
        sessionManager.start();

        //通过路径准备好类加载器,热加载默认关闭
        loader = new TommyWebAppLoader(appPath, true, this);
        loader.start();

        //解析web.xml内容
        context = new TommyContextConfig(appPath);

        //获取域对象
        servletContext = new TommyServletContext(context.getContextInitParameters(), appPath);

        //加载所有的filter
        initAllFilter(context.getFilterConfigMap());

        //初始化子容器
        loadWrappers();

        //一个一个启动子容器
        //我这里没有去做类型判断，因为我觉得这样好像没有毛病
        wrappers.values().forEach(TommyWrapper::start);


        //现在就是可以访问的了
        isPaused = false;

        //开始后台线程
        threadStart();

    }

    @Override
    protected void doStop() {

        isPaused = true;

        //先把后台线程停止了，这里用的是tomcat的源码的处理方法
        threadStop();

        //停止子容器
        wrappers.values().forEach(TommyWrapper::stop);
    }


    private void threadStart()
    {
        System.out.println("后代线程开始运行");
        backgroundThread = new Thread(() -> {
        threadDone = false;

            while (!threadDone) {
                System.out.println("后台检查ing：");
                loader.backgroundProcess();
                sessionManager.backgroundProcess();
                try {
                    //原版默认设置的就是15秒
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        },"Lehr's Background Process");

        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    /**
     * Stop the background thread that is periodically checking for
     * session timeouts.
     */
    private void threadStop() {

        System.out.println("后代线程停止运行");

        if (backgroundThread == null)
            return;

        threadDone = true;
        backgroundThread.interrupt();

        //FIXME: 我也不知道为什么 这里调用interrupt居然没用，所以只能暴力来了
        //backgroundThread.stop();
        //FIXME: 好吧不知道为什么 stop也被阻塞了？？！！

        try {
            backgroundThread.join();

        } catch (InterruptedException e) {
            // Ignore
        }

        backgroundThread = null;

    }


    public void reload() {

        stop();

        start();

        System.out.println("重载完成！！！！!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }


    /**
     * The background thread completion semaphore.
     */
    private volatile boolean threadDone = false;

    private Thread backgroundThread;



}

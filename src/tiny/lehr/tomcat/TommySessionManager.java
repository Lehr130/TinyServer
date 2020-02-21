package tiny.lehr.tomcat;

import tiny.lehr.tomcat.bean.TommySession;
import tiny.lehr.tomcat.container.TommyContext;
import tiny.lehr.tomcat.lifecircle.TommyLifecycle;
import tiny.lehr.tomcat.lifecircle.TommyLifecycleListener;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lehr
 * @create: 2020-02-15
 *
 * Session的创建是：当且调用了getSession(true)之后！！！
 */
public class TommySessionManager implements TommyLifecycle {


    //实际上这里还应该有一个属性来限制内存里的session的量的
    //还有一个换出机制，把session永久化

    private Map<String, TommySession> sessionPool;

    private TommyContext container;

    private TommyStoreManager storeManager;

    public TommySessionManager(TommyContext container)
    {
        this.container = container;
    }


    public HttpSession findSession(String sessionId)
    {
        TommySession session = sessionPool.get(sessionId);
        if(session!=null)
        {
            //检查是否过期：
            //session的过期时间是从session不活动的时候开始计算
            // 如果session一直活动，session就总不会过期
            // 从该Session未被访问,开始计时
            // 一旦Session被访问,计时清0;
            Long lastTime = session.getLastAccessedTime();
            Long validTime = session.getMaxInactiveInterval()*1000L;


            //FIXME:这里没有考负数作为永久有效的判定情况

            if(lastTime+validTime<System.currentTimeMillis())
            {
                //过期了
                System.out.println("过期了！");
                removeSession(sessionId);
                return null;
            }
            //这里还需要考虑session的删除问题

            //修改最近的访问时间
            session.setLastAccessedTime(System.currentTimeMillis());

        }

        return session;
    }

    public HttpSession createSession()
    {

        String sessionId = getSessionId();
        TommySession session = new TommySession(this,sessionId,container.getServletContext());
        sessionPool.put(sessionId,session);

        return session;

    }

    private String getSessionId() {
        return "HeyLehr" + UUID.randomUUID().toString();
    }

    //销毁方法，具体是在session里面通过自己绑定的这个manager来调用从而销毁
    public void removeSession(String sessionId)
    {
        sessionPool.remove(sessionId);
    }














    @Override
    public void addLifecycleListener(TommyLifecycleListener listener) {

    }

    @Override
    public List<TommyLifecycleListener> findLifecycleListeners() {
        return null;
    }

    @Override
    public void removeLifecycleListener(TommyLifecycleListener listener) {

    }

    @Override
    public void start() throws Exception {
        System.out.println("sessionManager开始啦");


        storeManager = new TommyStoreManager();
        storeManager.start();

        sessionPool = storeManager.getSessions(container.getAppName());

        if(sessionPool==null)
        {
            sessionPool = new HashMap<>();
        }


    }

    @Override
    public void stop() throws Exception {

        storeManager.store(sessionPool,container.getAppName());

        storeManager.stop();
    }

    public void backgroundProcess() {
        clean();
    }

    //把多余过期的session清理了
    //FIXME: 线程安全
    private void clean()
    {
        sessionPool.values().forEach(session->{

            Long lastTime = session.getLastAccessedTime();
            Long validTime = session.getMaxInactiveInterval()*1000L;

            if(lastTime+validTime<System.currentTimeMillis())
            {
                //过期了
                System.out.println("过期了！");
                removeSession(session.getId());
            }
        });

    }
}

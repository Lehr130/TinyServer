package tiny.lehr.tomcat.bean;


import tiny.lehr.tomcat.TommySessionManager;
import tiny.lehr.utils.EnumerationUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.Serializable;
import java.util.*;

/**
 * @author Lehr
 * @create: 2020-02-17
 */
public class TommySession implements HttpSession, Serializable {

    private static final long serialVersionUID = -6601903208557464574L;

    private Map<String,Object> attributes = new HashMap<>();

    private long lastAccessedTime;

    private long creationTime;

    private String sessionId;

    //默认的有效期是半小时
    private int maxInactiveInterval = 30*60;

    private TommyServletContext servletContext;

    private TommySessionManager manager;

    public TommySession(TommySessionManager manager,String sessionId,TommyServletContext servletContext)
    {
        this.manager = manager;
        this.sessionId = sessionId;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
        this.servletContext = servletContext;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long time)
    {
        lastAccessedTime = time;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void invalidate() {
        //直接销毁自己
        this.manager.removeSession(sessionId);
    }

    @Override
    public boolean isNew() {
        //如果是刚创建的，则这里会返回true
        //比如客户没有带token或者token无效新创建了一个
        //下面这段代码就是，只有第一次会话的时候调用会返回true:
        //我的处理方式就是去直接比对时间，因为第二次调用的话后者就不一样了
        return creationTime==lastAccessedTime;
    }



    @Override
    public void setMaxInactiveInterval(int i) {
        //以秒为单位设置过期时间
        maxInactiveInterval = i;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        //我也不知道这个是啥，但是反正就不建议使用了
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    @Override
    public Object getValue(String s) {
        return getAttribute(s);
    }

    @Override
    //IMPROVE:这里我不确定，我看tomcat返回的是个集合Collection
    public Enumeration<String> getAttributeNames() {
        return EnumerationUtils.getEnumerationStringByMap(attributes);
    }

    @Override
    public String[] getValueNames() {
        // 将Map 的值转化为Set
        String[] str = new String[attributes.size()];
        return attributes.keySet().toArray(str);

    }

    @Override
    public void setAttribute(String s, Object o) {
        attributes.put(s,o);
    }


    @Override
    public void putValue(String s, Object o) {
        //这个貌似是老版本的东西，功能和attribute一样
        setAttribute(s,o);
    }

    @Override
    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    @Override
    public void removeValue(String s) {
        removeAttribute(s);
    }

}

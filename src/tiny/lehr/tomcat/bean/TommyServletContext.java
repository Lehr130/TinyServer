package tiny.lehr.tomcat.bean;

import tiny.lehr.utils.EnumerationUtils;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author Lehr
 * @create: 2020-01-22
 */
public class TommyServletContext implements ServletContext {


    private Map<String,String> paramMap;

    private String path;

    private Map<String,Object> attributeMap;

    public TommyServletContext(Map<String, String> paramMap, String path) {
        this.paramMap = paramMap;
        this.path = path;
        attributeMap = new HashMap<>();
    }


    @Override
    public String getContextPath() {
        return path;
    }

    @Override
    public ServletContext getContext(String s) {
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public String getMimeType(String s) {
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String s) {
        return null;
    }

    @Override
    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }

    @Override
    public Servlet getServlet(String s) throws ServletException {
        return null;
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        return null;
    }

    @Override
    public Enumeration<String> getServletNames() {
        return null;
    }

    @Override
    public void log(String s) {

    }

    @Override
    public void log(Exception e, String s) {

    }

    @Override
    public void log(String s, Throwable throwable) {

    }

    /**
     * 必须以/开头，或者没有的话自动补充一个，如果是\的话会报错的（反正在idea里是连编译都过不了的，但是原因是叫非法转意符）
     * 我就简单的理解成有/就不用补，没就自动补/
     * @param s
     * @return
     */
    @Override
    public String getRealPath(String s) {
        if(s.startsWith("/"))
        {
            s = s.replaceAll("[/]+", File.separator);
        }
        else
        {
            s = File.separator + s;
        }
        return path+ s;
    }

    @Override
    public String getServerInfo() {
        return null;
    }

    /**
     * 通过名字返回初始化参数的值
     * @param s
     * @return
     */
    @Override
    public String getInitParameter(String s) {
        return paramMap.get(s);
    }

    /**
     * 返回初始化web.xml文件里初始化后的参数
     * @return
     */
    @Override
    public Enumeration<String> getInitParameterNames() {

        return EnumerationUtils.getEnumerationStringByMap(paramMap);

    }

    /**
     * 我真不知道设置这个为public的意义是什么，tomcat里面就是写的你一调用我就给你报错，还给你设置成public
     * 真是是有毛病啊
     * @param s
     * @param s1
     * @return
     */
    @Override
    public boolean setInitParameter(String s, String s1) {
        return false;
    }

    /**
     * 获取全局属性
     * @param s
     * @return
     */
    @Override
    public Object getAttribute(String s) {
        return attributeMap.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {

        return EnumerationUtils.getEnumerationStringByMap(attributeMap);

    }

    /**
     * 设置全局属性
     * @param s
     * @param o
     */
    @Override
    public void setAttribute(String s, Object o) {
        attributeMap.put(s,o);
    }

    @Override
    public void removeAttribute(String s) {
        attributeMap.remove(s);
    }

    @Override
    public String getServletContextName() {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, String s1) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, String s1) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> set) {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;
    }

    @Override
    public void addListener(String s) {

    }

    @Override
    public <T extends EventListener> void addListener(T t) {

    }

    @Override
    public void addListener(Class<? extends EventListener> aClass) {

    }

    @Override
    public <T extends EventListener> T createListener(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void declareRoles(String... strings) {

    }

    @Override
    public String getVirtualServerName() {
        return null;
    }
}

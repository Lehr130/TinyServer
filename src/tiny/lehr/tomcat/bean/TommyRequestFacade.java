package tiny.lehr.tomcat.bean;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * @author Lehr
 * @create: 2020-02-18
 */
public class TommyRequestFacade implements HttpServletRequest {

    HttpServletRequest req;

    public TommyRequestFacade(HttpServletRequest req)
    {
        this.req = req;
    }

    @Override
    public String getAuthType() {
        return req.getAuthType();
    }

    @Override
    public Cookie[] getCookies() {
        return req.getCookies();
    }

    @Override
    public long getDateHeader(String s) {
        return req.getDateHeader(s);
    }

    @Override
    public String getHeader(String s) {
        return req.getHeader(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return req.getHeaders(s);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return req.getHeaderNames();
    }

    @Override
    public int getIntHeader(String s) {
        return req.getIntHeader(s);
    }

    @Override
    public String getMethod() {
        return req.getMethod();
    }

    @Override
    public String getPathInfo() {
        return req.getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        return req.getPathTranslated();
    }

    @Override
    public String getContextPath() {
        return req.getContextPath();
    }

    @Override
    public String getQueryString() {
        return req.getQueryString();
    }

    @Override
    public String getRemoteUser() {
        return req.getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String s) {
        return req.isUserInRole(s);
    }

    @Override
    public Principal getUserPrincipal() {
        return req.getUserPrincipal();
    }

    @Override
    public String getRequestedSessionId() {
        return req.getRequestedSessionId();
    }

    @Override
    public String getRequestURI() {
        return req.getRequestURI();
    }

    @Override
    public StringBuffer getRequestURL() {
        return req.getRequestURL();
    }

    @Override
    public String getServletPath() {
        return req.getServletPath();
    }

    @Override
    public HttpSession getSession(boolean b) {
        return req.getSession(b);
    }

    @Override
    public HttpSession getSession() {
        return req.getSession();
    }

    @Override
    public String changeSessionId() {
        return req.changeSessionId();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return req.isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return req.isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return req.isRequestedSessionIdFromURL();
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return req.isRequestedSessionIdFromURL();
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return req.authenticate(httpServletResponse);
    }

    @Override
    public void login(String s, String s1) throws ServletException {
        req.login(s,s1);
    }

    @Override
    public void logout() throws ServletException {
        req.logout();
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return req.getParts();
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return req.getPart(s);
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return req.upgrade(aClass);
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return req.getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return req.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        req.setCharacterEncoding(s);
    }

    @Override
    public int getContentLength() {
        return req.getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return req.getContentLengthLong();
    }

    @Override
    public String getContentType() {
        return req.getContentType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return req.getInputStream();
    }

    @Override
    public String getParameter(String s) {
        return req.getParameter(s);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return req.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String s) {
        return req.getParameterValues(s);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        //这里是为了安全性，所以返回的是一个只读表！！！
        return Collections.unmodifiableMap(req.getParameterMap());
    }

    @Override
    public String getProtocol() {
        return req.getProtocol();
    }

    @Override
    public String getScheme() {
        return req.getScheme();
    }

    @Override
    public String getServerName() {
        return req.getServerName();
    }

    @Override
    public int getServerPort() {
        return req.getServerPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return req.getReader();
    }

    @Override
    public String getRemoteAddr() {
        return req.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return req.getRemoteHost();
    }

    @Override
    public void setAttribute(String s, Object o) {
        req.setAttribute(s,o);
    }

    @Override
    public void removeAttribute(String s) {
        req.removeAttribute(s);
    }

    @Override
    public Locale getLocale() {
        return req.getLocale();
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return req.getLocales();
    }

    @Override
    public boolean isSecure() {
        return req.isSecure();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return req.getRequestDispatcher(s);
    }

    @Override
    public String getRealPath(String s) {
        return req.getRealPath(s);
    }

    @Override
    public int getRemotePort() {
        return req.getRemotePort();
    }

    @Override
    public String getLocalName() {
        return req.getLocalName();
    }

    @Override
    public String getLocalAddr() {
        return req.getLocalAddr();
    }

    @Override
    public int getLocalPort() {
        return req.getLocalPort();
    }

    @Override
    public ServletContext getServletContext() {
        return req.getServletContext();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return req.startAsync();
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return req.startAsync(servletRequest,servletResponse);
    }

    @Override
    public boolean isAsyncStarted() {
        return req.isAsyncStarted();
    }

    @Override
    public boolean isAsyncSupported() {
        return req.isAsyncSupported();
    }

    @Override
    public AsyncContext getAsyncContext() {
        return req.getAsyncContext();
    }

    @Override
    public DispatcherType getDispatcherType() {
        return req.getDispatcherType();
    }
}

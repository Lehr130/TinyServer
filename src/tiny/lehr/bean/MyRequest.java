package tiny.lehr.bean;

import tiny.lehr.config.ConfigFacade;
import tiny.lehr.enums.Message;
import tiny.lehr.enums.ServerType;
import tiny.lehr.exceptions.BadRequestMethodException;
import tiny.lehr.exceptions.IllegalParamInputException;
import tiny.lehr.tomcat.TommySessionManager;
import tiny.lehr.tomcat.container.TommyContext;
import tiny.lehr.utils.EnumerationUtils;
import tiny.lehr.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.Socket;
import java.security.Principal;
import java.util.*;

/**
 * @author Lehr
 * @create 2020-01-14
 * 一个实现了ServletRequest的Request
 * 用于Servlet请求
 * FIXME 写一个RequestBase来！！！！
 */
public class MyRequest implements HttpServletRequest {

    private String method;

    private String uri;

    private String host;

    private String jSessionId;

    private Integer port;

    private String remoteHost;

    private Integer remotePort;

    private List<Cookie> cookies = new ArrayList<>();

    /**
     * 感觉和host是一样的
     */
    private String remoteAddress;

    private String protocol;

    private Map<String, String> headers;

    private Map<String,String[]> parameters;

    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 指向当前应用容器的指针
     * 在请求在context invoke之前会把这个值写好
     */
    private TommyContext context;

    private ServerType serverType;

    private String servletPath;

    private String contextPath;


    public ServerType getServerType() {
        return serverType;
    }

    public void setContext(TommyContext context)
    {
        this.context = context;
    }

    public MyRequest(String method)
    {
        this.method = method;
    }

    public MyRequest(Socket socket) throws IOException, IllegalParamInputException, BadRequestMethodException {

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Integer bufferSize = 1024;

        // 这里其实应该循环读入
        char[] buffer = new char[bufferSize];

        StringBuilder sb = new StringBuilder();

        // read()方法当读取完数据之后就开始阻塞，等待返回-1结束，这个等待的过程占到了总时间的99%

        Integer count = bufferSize;
        while (count.equals(bufferSize)) {
            count = br.read(buffer);
            sb.append(buffer);
        }

        String request = new String(sb);

        System.out.println("--------------------------------");
        //不知道为什么有些时候就会收到空请求 似乎是因为socket阻塞了？？？


       // System.out.println(request);
        System.out.println("--------------------------------");


        packUp(request);


        remoteAddress = socket.getInetAddress().getHostAddress();
        //FIXME:暂时不知道有什么区别
        remoteHost = remoteAddress;
        remotePort = socket.getPort();

        // 收完报文后半关闭
        socket.shutdownInput();
    }


    public void packUp(String request) throws IllegalParamInputException, BadRequestMethodException, IOException {

        String[] requestLine = request.split("\r\n");

        // 处理请求行 "POST /path/uri HTTP/1.1"
        packLine(requestLine[0]);

        Integer count;

        this.headers = new HashMap<>(16);

        // 处理请求头 "Connection: keep-alive" 多行
        for (count = 1; count < requestLine.length && requestLine[count].length() > 0; count++) {
            String[] header = requestLine[count].split(": ");
            headers.put(header[0], header[1]);
        }

        //把有些特殊的请求头放到特定的位置
        //cookie的内容：abc=xxxxxx;def=xxxx;adfa=eeee
        String cookieStr = headers.get("Cookie");

        if(cookieStr!=null&&cookieStr.length()>0)
        {
            String[] cookieStrs = cookieStr.split(";");
            for (String s : cookieStrs) {
                String[] str = s.split("=");

                //特殊情况处理一下sessionID  //这里注意一下所有的key前面都有空格!!!
                String key = str[0].trim();
                String value = str[1];

                if("JSESSIONID".equalsIgnoreCase(key))
                {
                    jSessionId = value;
                }
                //这里注意一个万恶的bug
                //cookie的字符串里不能有空格不然会报错tmd
                cookies.add(new Cookie(key,value));
            }

        }




        String hostPort = headers.get("Host");

        host = hostPort.split(":")[0];

        port = Integer.valueOf(hostPort.split(":")[1]);


        // 跳过空行， 如果有请求体的话
        Boolean isFirst = true;
        if (requestLine[++count].trim().length() > 0) {
            //把请求头里的参数带上！
            if(uri.contains("?"))
            {
                int paraIndex = uri.indexOf('?');
                String uriPara = uri.substring(paraIndex+1);
                uri = uri.substring(0, paraIndex);
                requestLine[count] = requestLine[count] + "&" + uriPara;
            }
            // 处理请求体 "adsf=dsaf&adsf=dsaf"
            //这里注意，结构应该是string string[] 比如t1=1&t1=2这种checkBox的情况
            parameters = UrlUtils.getParamMap(requestLine[count]);
        }


        //判断类型
        checkType();


    }


    public void parseUrl()
    {
        contextPath = uri.substring(0,uri.indexOf("/",1));
        servletPath = uri.substring(uri.indexOf("/",1));

    }

    /**
     * 处理请求行
     *
     * @param line
     * @throws BadRequestMethodException
     */
    public void packLine(String line) throws BadRequestMethodException {

        System.out.println("请求头是："+line.length());



        // 切片
        String[] reh = line.split(" ");

        //获取请求方法
        method = reh[0];

        // 获取uri
        uri = reh[1];

        // 获取HTTP协议版本
        this.protocol = reh[2];




    }

    private void checkType() throws IllegalParamInputException, FileNotFoundException, IOException {

        // 先做代理判断
        String proxyUri = ConfigFacade.getInstance().getProxy(uri);
        if (proxyUri!=null) {
            serverType = ServerType.PROXY;
            return ;
        }

        // 处理默认目录
        if ('/' == uri.charAt(uri.length() - 1)) {
            uri = uri + Message.DEFAULT_SUFFIX;
        }


        // 这是一段很迷惑的正则表达式.....
        String regex = "^/[-A-Za-z0-9./]+[.][A-Za-z]+[?]?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+?";
        // 判断是静态文件
        if (uri.matches(regex)) {

            String filename = Message.ROOT_PATH + uri;

            serverType = ServerType.STATIC_RESOURCES;
            return ;
        }


        //Dynamic请求判断
        if(uri.startsWith("/dynamic/"))
        {
            serverType = ServerType.DYNAMIC_JAVA;

        }
        // 判断是动态文件
        else {
            serverType = ServerType.SERVLET;
            return ;
        }

    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return cookies.toArray(new Cookie[cookies.size()]);
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return headers.get(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return EnumerationUtils.getEnumerationStringByMap(headers);
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    //获取从cookie里得到的sessionId 如果没有的话就是null
    public String getRequestedSessionId() {
        return jSessionId;
    }

    @Override
    /**
     * 这个是uri
     * 比如
     *    /TommyTest_war_exploded/SayHeyServlet
     */
    public String getRequestURI() {
        return uri;
    }

    /**
     * 这个是完整的url
     * 比如：http://192.168.0.108:8080/TommyTest_war_exploded/SayHeyServlet
     * TODO: 目前不知道怎么获取user http or https
     * @return
     */
    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer().append("http://")
                .append(host)
                .append(":"+ port)
                .append("/"+uri);

    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public HttpSession getSession(boolean b) {

        // FIXME:门面一下
        return doGetSession(b);

    }

    //原版是把这个方法写到了一个叫做requestBase的地方的
    private HttpSession doGetSession(boolean create)
    {
        if(context==null)
        {
            return null;
        }

        //获取当前上下文中的session管理器
        TommySessionManager sessionManager = context.getSessionManager();

        if(sessionManager==null)
        {
            return null;
        }

        //设置session
        HttpSession session = sessionManager.findSession(jSessionId);

        if(create==false)
        {
            return session;
        }

        //创建session
        if(session==null)
        {
            session = sessionManager.createSession();
            //响应的时候响应头里需要放东西，所以这里记录一下sessionId
            jSessionId = session.getId();
        }



        return session;
    }


    @Override
    public HttpSession getSession() {
        //其实就是调用上面的那个方法
        return getSession(true);
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }


    @Override
    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return EnumerationUtils.getEnumerationStringByMap(attributes);
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * 默认获取第一个参数，如果是多选框的话，则需要下面那个getParameterValue方法
     * @param s
     * @return
     */
    @Override
    public String getParameter(String s) {
        return parameters.get(s)[0];
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return EnumerationUtils.getEnumerationStringByMap(parameters);
    }

    @Override
    public String[] getParameterValues(String s) {
        return parameters.get(s);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        //这里是为了安全性，所以在facade包装之后返回的是一个只读表！！！
        return parameters;
    }


    @Override
    public String getProtocol() {
        //获取http协议版本
        return protocol;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    /**
     * 用户的addr 192.168.0.107
     * @return
     */
    @Override
    public String getRemoteAddr() {
        return remoteAddress;
    }

    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    @Override
    public void setAttribute(String s, Object o) {
        attributes.put(s,o);

    }

    @Override
    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        //TODO: 怎么获得请求主机的信息
        return 0000;//remotePort;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return context.getServletContext();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}

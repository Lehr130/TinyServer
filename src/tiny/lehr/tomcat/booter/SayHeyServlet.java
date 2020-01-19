package tiny.lehr.tomcat.booter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Lehr
 * @create: 2020-01-17
 */
public class SayHeyServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("I'm now Initing ----SayHeyServlet");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("I'm now Serving ----SayHeyServlet");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}

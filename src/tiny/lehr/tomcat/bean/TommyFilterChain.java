package tiny.lehr.tomcat.bean;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-01-23
 * 用来表示一个过滤器链的类
 * 内部实现对所有过滤器的递归调用
 */
public class TommyFilterChain implements FilterChain {

    /**
     * 用于表示内部的过滤器，按照顺序排列
     */
    List<Filter> filterList = new ArrayList<>();

    /**
     * 本条过滤器最终所对应的要执行的那个Servlet
     */
    Servlet servlet;

    /**
     * 用来计数，递归的时候通过这个数字来依次触发filter
     */
    Integer counter = 0;

    /**
     * 添加一个filter
     * @param filter
     */
    public void addFilter(Filter filter)
    {
        filterList.add(filter);
    }

    /**
     * 设置目标Servlet
     * @param servlet
     */
    public void setServlet(Servlet servlet)
    {
        this.servlet = servlet;
    }


    public void release()
    {
        filterList.forEach(i->i=null);

        counter = 0;

        servlet = null;
    }
    /**
     * 递归依次实现过滤链里的每个方法
     * @param req
     * @param res
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res) throws IOException, ServletException {

        //如果没有递归完
            if(filterList.size()>counter)
            {
                //继续执行filter
                filterList.get(counter++).doFilter(req,res,this);
            }
            else
            {
                //执行完了filter就触发servlet的方法
                servlet.service(req,res);

            }

    }



}

package tiny.lehr.tomcat.bean;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-01-23
 */
public class TommyFilterChain implements FilterChain {

    List<Filter> filterList = new ArrayList<>();

    Servlet servlet;

    Integer counter = 0;

    public Boolean noFilter()
    {
        if(filterList.size()==0)
        {
            return true;
        }
        return false;
    }

    public void addFilter(Filter filter)
    {
        filterList.add(filter);
    }

    public void setServlet(Servlet servlet)
    {
        this.servlet = servlet;
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res) throws IOException, ServletException {


            if(filterList.size()>counter)
            {
                System.out.println(filterList);
                System.out.println(filterList.get(counter));
                filterList.get(counter++).doFilter(req,res,this);
            }
            if(filterList.size()==counter)
            {
                servlet.service(req,res);
                //记得清0，因为我这种设计里面一条链子只会被实例化一次的
                counter = 0 ;
            }

    }
}

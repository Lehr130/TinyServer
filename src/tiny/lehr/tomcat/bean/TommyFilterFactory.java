package tiny.lehr.tomcat.bean;


import tiny.lehr.tomcat.container.TommyWrapper;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

;

/**
 * @author Lehr
 * @create: 2020-01-23
 */
public class TommyFilterFactory {

    Map<TommyFilterConfig,Filter> filterPool = new HashMap<>();

    Map<TommyWrapper,TommyFilterChain> filterChainCache = new HashMap<>();

    public TommyFilterFactory(Map<String, TommyFilterConfig> filterConfigMap, TommyWebAppLoader loader, ServletContext servletContext) {

        filterConfigMap.forEach((filterName,filterConfig)->
        {
            try{

                filterConfig.setServletContext(servletContext);
                Filter myFilter = (Filter) loader.loadClass(filterConfig.getFilterClassName()).getDeclaredConstructor().newInstance();
                myFilter.init(filterConfig);
                filterPool.put(filterConfig,myFilter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

    }



    public FilterChain getFilterChain(TommyWrapper wrapper)
    {

        return  filterChainCache.get(wrapper);

    }

    public void createFilterChain(String url, TommyWrapper wrapper)
    {

        TommyFilterChain chain = new TommyFilterChain();
        chain.setServlet(wrapper.getMyServlet());

        filterPool.forEach((config,filter)->
        {
            //TODO 如果url满足要求就加入chain 以后写个判定算法
            Boolean flag = url.equals(config.getFilterUrl());
            if(flag)
            {
                System.out.println(config.getFilterName());
                chain.addFilter(filter);
            }

            //TODO 如果servlet-name满足要求也加入（虽然现在还没实现这个）
            if(false)
            {
                chain.addFilter(filter);
            }


        });

        filterChainCache.put(wrapper,chain);

    }
}

package tiny.lehr.tomcat;

import tiny.lehr.tomcat.bean.TommyServletConfig;
import tiny.lehr.tomcat.bean.TommyServletContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-19
 * 给Context容器使用的路径映射器
 * 用来找到正确的Wrapper
 * 个人感觉这个类里面的方法设计从命名上和调用上都很烂......需要优化！！！
 */
public class TommyMapper {

    /**
     * 这个Map是映射器的核心：给一个url就能获得该servlet的对应信息，可以用来初始化
     *
     */
    private Map<String, TommyServletConfig> wrapperMap;


    /**
     * 构造方法 : 读取相应目录下的配置文件web.xml并解析到map里去
     * 如果配置文件有错就会爆炸
     *
     * @param parser
     */
    public TommyMapper(TommyXmlParser parser, TommyServletContext context) {

        wrapperMap = new HashMap<>();


        parser.getServletConfigMap().forEach((k,v)->{
            v.setServletContext(context);
            wrapperMap.put(v.getServletUrl(),v);
        });


    }

    /**
     * 本类的核心功能点：通过url查找类名
     *
     * @param servletUrl
     * @return
     */
    public TommyServletConfig getServletClass(String servletUrl) {

        return wrapperMap.get(servletUrl);

    }


}


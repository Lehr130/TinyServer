package tiny.lehr.tomcat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tiny.lehr.tomcat.bean.TommyFilterConfig;
import tiny.lehr.tomcat.bean.TommyServletDef;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-01-22
 * 这个类专门用来对每个Webapp的web.xml进行解析
 */
public class TommyContextConfig {

    private Document xmlDoc;

    /**
     * 以url为Key
     */
    private Map<String, TommyServletDef> servletDefMap;

    private Map<String, String> contextInitParameters;

    private Map<String, TommyFilterConfig> filterConfigMap;





    public TommyContextConfig(String path) throws Exception {
        //把web.xml解析为一棵dom树
        File xmlFile = new File(path + File.separator + "WEB-INF" + File.separator + "web.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        xmlDoc = builder.parse(xmlFile);
        servletDefMap = new HashMap<>();
        filterConfigMap = new HashMap<>();
        contextInitParameters = new HashMap<>();

        parseFilter();
        parseServlet();
        getContextParams();


    }

    public Map<String, TommyServletDef> getServletDefMap() {
        return servletDefMap;
    }

    public Map<String, String> getContextInitParameters() {
        return contextInitParameters;
    }

    public Map<String, TommyFilterConfig> getFilterConfigMap() {
        return filterConfigMap;
    }

    private void parseServlet() {

        //解析servlet标签
        NodeList servletNodeList = xmlDoc.getElementsByTagName("servlet");
        for (int i = 0; i < servletNodeList.getLength(); i++) {
            Element node = (Element) servletNodeList.item(i);

            //获取名字
            String servletName = getByTag(node, "servlet-name");

            //获取类
            String servletClass = getByTag(node, "servlet-class");

            //加入map
            servletDefMap.put(servletName, new TommyServletDef(servletName, servletClass, getInitParamsMap(node)));
        }

        //解析Servlet-mapping标签
        NodeList mappingNodeList = xmlDoc.getElementsByTagName("servlet-mapping");
        for (int i = 0; i < mappingNodeList.getLength(); i++) {
            Element node = (Element) mappingNodeList.item(i);

            //从集合里搜索出名字一样的servletConfig
            TommyServletDef config = servletDefMap.get(getByTag(node, "servlet-name"));

            //放入url
            config.setServletUrl(getByTag(node, "url-pattern"));

        }

    }

    private void parseFilter() {
        //解析Filter

        //解析servlet标签
        NodeList filterNodeList = xmlDoc.getElementsByTagName("filter");
        for (int i = 0; i < filterNodeList.getLength(); i++) {
            Element node = (Element) filterNodeList.item(i);

            //获取名字
            String filterName = getByTag(node, "filter-name");

            //获取类
            String filterClass = getByTag(node, "filter-class");

            //加入map
            filterConfigMap.put(filterName, new TommyFilterConfig(filterName, filterClass, getInitParamsMap(node)));
        }

        //解析Servlet-mapping标签
        NodeList mappingNodeList = xmlDoc.getElementsByTagName("filter-mapping");
        for (int i = 0; i < mappingNodeList.getLength(); i++) {
            Element node = (Element) mappingNodeList.item(i);

            //从集合里搜索出名字一样的servletConfig
            TommyFilterConfig config = filterConfigMap.get(getByTag(node, "filter-name"));

            //放入url
            config.setFilterUrl(getByTag(node, "url-pattern"));

        }
    }

    /**
     * 这也是封装的人家的api
     * 从一个标签里获得属性值
     *
     * @param node
     * @param tag
     * @return
     */
    private String getByTag(Element node, String tag) {
        return node.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue();
    }



    private void getContextParams() {

        NodeList contextParamNodeList = xmlDoc.getElementsByTagName("context-param");
        for (int i = 0; i < contextParamNodeList.getLength(); i++) {
            Element node = (Element) contextParamNodeList.item(i);

            //获取名字
            String paramName = getByTag(node, "param-name");

            //获取类
            String paramValue = getByTag(node, "param-value");

            //加入map
            contextInitParameters.put(paramName,paramValue);
        }
    }

    private Map<String,String> getInitParamsMap(Element node) {
        //获取参数
        Map<String, String> parametersMap = new HashMap<>();

        NodeList servletParamNodeList = node.getElementsByTagName("init-param");
        for (int j = 0; j < servletParamNodeList.getLength(); j++) {
            Element paramNode = (Element) servletParamNodeList.item(j);
            String paramName = getByTag(paramNode, "param-name");
            String paramValue = getByTag(paramNode, "param-value");
            parametersMap.put(paramName,paramValue);
        }

        return parametersMap;
    }


}

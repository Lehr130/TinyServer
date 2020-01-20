package tiny.lehr.tomcat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tiny.lehr.tomcat.bean.TommyHttpRequest;
import tiny.lehr.tomcat.container.TommyWrapper;
import tiny.lehr.tomcat.loader.TommyWebAppLoader;

import javax.servlet.ServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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
     * 这个Map是映射器的核心：给一个url就能获得类名
     */
    private Map<String, String> wrapperMap;


    /**
     * 构造方法 : 读取相应目录下的配置文件web.xml并解析到map里去
     * 如果配置文件有错就会爆炸
     *
     * @param path
     */
    public TommyMapper(String path) {


        //去WEB-INF下找到web.xml配置文件
        File xmlFile = new File(path + File.separator + "WEB-INF" + File.separator + "web.xml");

        //开始解析
        try {
            wrapperMap = parseXml(xmlFile);
        } catch (Exception e) {
            System.out.println("xml文件解析失败！！！");
            e.printStackTrace();
        }

    }

    /**
     * 本类的核心功能点：通过url查找类名
     *
     * @param servletUrl
     * @return
     */
    public String getServletClass(String servletUrl) {

        return wrapperMap.get(servletUrl);

    }

    /**
     * 聚合对web.xml里的servlet的映射结果并返回一个map
     *
     * @param xmlFile
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Map<String, String> parseXml(File xmlFile) throws ParserConfigurationException, IOException, SAXException {

        //利用w3c的xml解析接口
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        //得到servlet标签里的所有对应关系
        Map servletMap = getMapFromElement(doc, "servlet", "servlet-name", "servlet-class");

        //得到servlet-mapping标签里的所有对应关系
        Map mappingMap = getMapFromElement(doc, "servlet-mapping", "servlet-name", "url-pattern");

        //聚合后返回
        return getAggrMap(servletMap, mappingMap);

    }

    /**
     * 聚合得到servlet的类名和映射关系
     *
     * @param servletMap
     * @param mappingMap
     * @return
     */
    private Map<String, String> getAggrMap(Map<String, String> servletMap, Map<String, String> mappingMap) {

        Map<String, String> aggrMap = new HashMap<>();

        //lambda妙哉
        servletMap.forEach((k, v) -> {
            aggrMap.put(mappingMap.get(k), v);
        });

        return aggrMap;
    }

    /**
     * 这就是封装的别的api而已
     * 用来从一个大标签里获得map
     *
     * @param doc
     * @param elementName
     * @param tag1
     * @param tag2
     * @return
     */
    private Map<String, String> getMapFromElement(Document doc, String elementName, String tag1, String tag2) {
        Map<String, String> map = new HashMap<>();
        NodeList nodeList = doc.getElementsByTagName(elementName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element node = (Element) nodeList.item(i);
            map.put(getByTag(node, tag1), getByTag(node, tag2));
        }
        return map;
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


}


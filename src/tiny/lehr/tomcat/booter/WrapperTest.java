package tiny.lehr.tomcat.booter;

import tiny.lehr.enums.Message;
import tiny.lehr.tomcat.bean.TommyHttpRequest;
import tiny.lehr.tomcat.bean.TommyHttpResponse;
import tiny.lehr.tomcat.bean.TommyRequest;
import tiny.lehr.tomcat.bean.TommyResponse;
import tiny.lehr.tomcat.container.TommyContext;
import tiny.lehr.tomcat.valve.SayHeyValve;
import tiny.lehr.tomcat.valve.TommyValve;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

/**
 * @author Lehr
 * @create: 2020-01-17
 */
public class WrapperTest {

    public static void main(String[] args) throws ServletException, IOException {


        String webappName = "TommyTest";

        //创建一个context

        //让这个context对应一个war（目前是文件形式的）

        TommyContext context = new TommyContext(Message.SERVLET_PATH + File.separator + webappName);

        TommyValve valve1 = new SayHeyValve();

        context.addValve(valve1);


        //javax.servlet.ServletException: non-HTTP request or response
        //在servlet.service这个方法调用的时候好像会自动检查非空的情况，还有请求类型匹配否，这里实验用的是httpServlet所以必须是httpServletRequest之类的实例才可以

        TommyHttpRequest req = new TommyHttpRequest();

        TommyHttpResponse res = new TommyHttpResponse();

        req.setMethod("GET");

        req.setN("/SayHeyServlet");

        context.invoke(req, res);

        req.setN("/SayByeServlet");

        context.invoke(req, res);

        req.setN("/SayHeyServlet");

        context.invoke(req, res);

        //加入两个阀门试试

//        TommyWrapper wrapper = new TommyWrapper();
//        wrapper.setServletClass("SayHeyServlet");
//        TommyLoader loader = new TommyLoader();
//        TommyValve valve1 = new SayHeyValve();

//        wrapper.setLoader(loader);
//        wrapper.addValve(valve1);
//
//        wrapper.invoke(req,res);

        //((Pipeline) wrapper).addValve(valve1);
        //((Pipeline) wrapper).addValve(valve2);

        //connector.setContainer(wrapper);


    }
}

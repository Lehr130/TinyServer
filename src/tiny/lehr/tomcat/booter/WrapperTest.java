package tiny.lehr.tomcat.booter;

import tiny.lehr.enums.Message;
import tiny.lehr.tomcat.bean.TommyHttpRequest;
import tiny.lehr.tomcat.bean.TommyHttpResponse;
import tiny.lehr.tomcat.container.TommyEngine;

/**
 * @author Lehr
 * @create: 2020-01-17
 */
public class WrapperTest {

    public static void main(String[] args) throws Exception {




        TommyEngine engine = new TommyEngine();
        engine.addHost("localhost",Message.SERVLET_PATH);

        TommyHttpRequest req = new TommyHttpRequest();

        TommyHttpResponse res = new TommyHttpResponse();

        req.setMethod("GET");

        req.setE("localhost");

        req.setN("/SayByeServlet");

        req.setC("TommyTest");

        engine.invoke(req, res);




    }
}

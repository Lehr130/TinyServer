package tiny.lehr.tomcat.booter;

import tiny.lehr.tomcat.TommyRequest;
import tiny.lehr.tomcat.TommyResponse;
import tiny.lehr.tomcat.TommyLoader;
import tiny.lehr.tomcat.container.TommyWrapper;
import tiny.lehr.tomcat.valve.SayHeyValve;
import tiny.lehr.tomcat.valve.TommyValve;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author Lehr
 * @create: 2020-01-17
 */
public class WrapperTest {


    public static void main(String[] args) throws ServletException, IOException {

        TommyRequest req = null;
        TommyResponse res = null;
//
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

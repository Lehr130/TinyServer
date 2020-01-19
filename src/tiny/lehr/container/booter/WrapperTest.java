package tiny.lehr.container.booter;

import tiny.lehr.bean.TommyRequest;
import tiny.lehr.bean.TommyResponse;
import tiny.lehr.container.TommyLoader;
import tiny.lehr.container.TommyWrapper;
import tiny.lehr.container.valve.SayHeyValve;
import tiny.lehr.container.valve.TommyValve;

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

        TommyWrapper wrapper = new TommyWrapper();
        wrapper.setServletClass("SayHeyServlet");
        TommyLoader loader = new TommyLoader();
        TommyValve valve1 = new SayHeyValve();

        wrapper.setLoader(loader);
        wrapper.addValve(valve1);

        wrapper.invoke(req,res);

        //((Pipeline) wrapper).addValve(valve1);
        //((Pipeline) wrapper).addValve(valve2);

        //connector.setContainer(wrapper);


    }
}

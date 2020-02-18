package tiny.lehr.tomcat.valve;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;

/**
 * @author Lehr
 * @create: 2020-01-17
 */
public class SayHeyValve implements TommyValve {

    @Override
    public void invoke(MyRequest req, MyResponse res) {
        System.out.println("Hey, there is Valve One");
    }

}

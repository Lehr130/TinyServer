package tiny.lehr.container.valve;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Lehr
 * @create: 2020-01-17
 */
public class SayHeyValve implements TommyValve {

    @Override
    public void invoke(ServletRequest req, ServletResponse res) {
        System.out.println("Hey, there is Valve One");
    }

}

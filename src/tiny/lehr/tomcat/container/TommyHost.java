package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.container.TommyContainer;
import tiny.lehr.tomcat.container.TommyContext;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyHost extends TommyContainer {


    @Override
    void beforeInvoke() {

    }

    @Override
    void basicValveInvoke(ServletRequest req, ServletResponse res) {

    }
}

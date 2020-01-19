package tiny.lehr.tomcat.valve;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public interface TommyValve {

    /**
     * 每个阀的执行方法，必须把方法返回结果放到res里面去
     * @param req
     * @param res
     */
    void invoke(ServletRequest req, ServletResponse res);

}

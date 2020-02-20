package tiny.lehr.processor;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.bean.MyResponse;
import tiny.lehr.enums.Message;
import tiny.lehr.tomcat.container.TommyEngine;

/**
 * @author Lehr
 * @create 2020-01-14
 * 用来处理Servlet请求的处理器
 * 由于之前的结构问题，并不能像Tomcat一样直接拿输出流回传信息
 * 这样，设计的时候给ServletResponse传入的不是socket的writer,
 * 而是给一个新的writer,获取输入之后再统一合并到后面的步骤去
 */
public class ServletProcessor extends Processor {


    private static TommyEngine engine = new TommyEngine();

    static {
        try {
            engine.addHost("localhost", Message.SERVLET_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareData(MyRequest req, MyResponse res) throws Exception {

        //这里做个处理：如果是servlet请求，就先把他的容器路径解析出来
        req.parseUrl();

        if(engine.isStarted()==false)
        {
            engine.start();
        }

        engine.invoke(req, res);
    }
}

package tiny.lehr.tomcat;

import tiny.lehr.bean.MyRequest;
import tiny.lehr.tomcat.container.TommyContainer;

/**
 * @author Lehr
 * @create: 2020-02-20
 */
public class TommyMapper {

    //通过这种方式来绑定容器
    private TommyContainer container;

    public TommyContainer getContainer() {
        return container;
    }

    public void setContainer(TommyContainer container) {
        this.container = container;
    }


    public TommyContainer map(MyRequest res)
    {
        return null;
    }


}

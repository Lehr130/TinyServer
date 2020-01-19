package tiny.lehr.container;

import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyHost implements TommyContainer {


    private String hostname;

    /**
     * 用来装载Wrapper
     */
    private Map<String,TommyContext> contextMap;

    public void loadHost(){};

    public void addContext(){};

    public void removeContext(){};

}

package tiny.lehr.container;

import java.util.Map;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyContext implements TommyContainer {


        private String warPath;

        /**
         * 用来装载Wrapper
         */
        private Map<String,TommyWrapper> wrapperMap;

        public void loadWar(){};

        public void addWrapper(){};

        public void removeWrapper(){};



}

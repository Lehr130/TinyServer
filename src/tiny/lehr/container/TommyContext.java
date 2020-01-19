package tiny.lehr.container;

import tiny.lehr.container.valve.TommyValve;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
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
        private Map<String,TommyWrapper> wrapperMap = new HashMap<>(16);

        private TommyPipeline pipeline = new TommyPipeline();

        public void setBasic()
        {

                //set your context valve here as its basic valve

                //the context valve will invoke the right wrapper

                //which is actully invoke the whole pipline of the wrapper

                TommyValve wrapperValve;
                pipeline.setBasicValve(wrapperValve);
        }

        public void loadWar(){};

        public void addWrapper(){};

        public void removeWrapper(){};

        public class TommyContextValve implements TommyValve{


                @Override
                public void invoke(ServletRequest req, ServletResponse res) {

                        //selectWrapper();

                        TommyWrapper tommyWrapper = null;

                        tommyWrapper.invoke(req,res);

                }
        }



}

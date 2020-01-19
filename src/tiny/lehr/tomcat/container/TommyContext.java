package tiny.lehr.tomcat.container;

import tiny.lehr.tomcat.TommyMapper;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Lehr
 * @create 2020-01-16
 */
public class TommyContext extends TommyContainer {

        //Use this mapper to get the right wrapper
        private TommyMapper mapper = new TommyMapper();

        //the wrapper that mapping an unique url
        private TommyWrapper wrapper;

        //loading a war package
        public void loadWarPackage()
        {

        }

        //after getting the infos from web.xml in the war package, mapping them down here
        public void initMapper()
        {

        }


        @Override
        public void beforeInvoke() {

        }

        @Override
        public void basicValveInvoke(ServletRequest req, ServletResponse res) {

                //find wrapper and init wrapper by using the TommyLoader
                wrapper = mapper.getWrapper(req,getLoader());

                //invoke it
                try {
                        wrapper.invoke(req, res);
                } catch (ServletException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }


        }
}

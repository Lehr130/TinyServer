package tiny.lehr.tomcat;

import tiny.lehr.enums.Message;
import tiny.lehr.tomcat.bean.TommySession;
import tiny.lehr.tomcat.lifecircle.TommyLifecycle;
import tiny.lehr.tomcat.lifecircle.TommyLifecycleListener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-02-21
 * 这个其实就是简单版的给session持久化的组件
 */
public class TommyStoreManager implements TommyLifecycle {


    //默认全部放在servlet容器顶层文件夹
    private String persistantPath = Message.SERVLET_PATH+ File.separator+"/sessionStorage";

    private static final String PREFIX = "sessionStorage_";

    //每次加载好了是会把文件删除的！！！所以这里也要新建文件
    public void store(Map<String, TommySession> sessions,String appName) throws Exception
    {

        File f = new File(persistantPath+File.separator+PREFIX+File.separator+appName);
        if(f.exists())
        {
            f.delete();
        }


        f.createNewFile();

        if(sessions==null)
        {
            f.delete();
            return ;
        }


        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));)
        {
            List<TommySession> sessionList = new ArrayList<>();

            sessionList.addAll(sessions.values());

            System.out.println(sessionList.size());

            oos.writeObject(sessionList);

            oos.flush();

        }



    }

    public Map<String, TommySession> getSessions(String appName)
    {
        Map<String, TommySession> sessions = new HashMap<>();

        File f = new File(persistantPath+File.separator+PREFIX+File.separator+appName);
        if(!f.exists())
        {
            return null;
        }


        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));)
        {
            List<TommySession> tommySessions = (List<TommySession>) in.readObject();

            tommySessions.forEach(s->sessions.put(s.getId(),s));

        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取完之后要把文件删除了！！！
        f.delete();

        return sessions;
    }






    @Override
    public void addLifecycleListener(TommyLifecycleListener listener) {

    }

    @Override
    public List<TommyLifecycleListener> findLifecycleListeners() {
        return null;
    }

    @Override
    public void removeLifecycleListener(TommyLifecycleListener listener) {

    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }
}

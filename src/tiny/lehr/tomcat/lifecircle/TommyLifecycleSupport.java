package tiny.lehr.tomcat.lifecircle;

import java.util.ArrayList;
import java.util.List;

/**
 * 生命周期管理的实例类
 * 提供对观察者的添加，删除及通知观察者的方法。（Tomcat release 9.0.x版本中没有用到这个）
 */
public final class TommyLifecycleSupport {


    private TommyLifecycle lifecycle;

    //内置的监听器些 原版用的数组，我觉得怪麻烦的，所以就用list了  TODO:为啥他喜欢用数组？
    private List<TommyLifecycleListener> listeners = new ArrayList<>();

    public TommyLifecycleSupport(TommyLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    //添加监听器，该方法中动态数组的方法值得借鉴
    public void addLifecycleListener(TommyLifecycleListener listener) {

      synchronized (listeners) {

          listeners.add(listener);
      }

    }
    public List<TommyLifecycleListener> findLifecycleListeners() {

        return listeners;

    }

    //这个其实就是通知大伙
    public void fireLifecycleEvent(String type, Object data) {

        TommyLifecycleEvent event = new TommyLifecycleEvent(lifecycle, type, data);

        synchronized (listeners) {
        listeners.forEach(l->l.lifecycleEvent(event));
        }

    }
    public void removeLifecycleListener(TommyLifecycleListener listener) {

        synchronized (listeners) {

            listeners.forEach(l->{
                if(listener.equals(l))
                {
                    //这个remove的方法的实现其实就是遍历然后对比
                    //TODO: https://www.jianshu.com/p/c1fb6a26a136 这里有个有趣的情况
                    listeners.remove(l);
                }
            });

        }

    }


}

package tiny.lehr.tomcat.lifecircle;

/**
 * 抽象观察者
 */
public interface TommyLifecycleListener {

    void lifecycleEvent(TommyLifecycleEvent event);

}
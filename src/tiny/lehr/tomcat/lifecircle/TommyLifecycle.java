package tiny.lehr.tomcat.lifecircle;

import java.util.List;

/**
 * 抽象主题
 */
public interface TommyLifecycle {


    String START_EVENT = "start";

    String BEFORE_START_EVENT = "before_start";

    String BEFORE_INIT_EVENT = "before_init";

    String AFTER_INIT_EVENT = "after_init";

    String AFTER_START_EVENT = "after_start";

    String STOP_EVENT = "stop";

    String BEFORE_STOP_EVENT = "before_stop";

    String AFTER_STOP_EVENT = "after_stop";

    void addLifecycleListener(TommyLifecycleListener listener);

    List<TommyLifecycleListener> findLifecycleListeners();

    void removeLifecycleListener(TommyLifecycleListener listener);

    void start() throws Exception;

    void stop() throws Exception;

}

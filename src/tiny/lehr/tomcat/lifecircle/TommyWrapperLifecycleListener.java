package tiny.lehr.tomcat.lifecircle;

/**
 * @author Lehr
 * @create: 2020-02-19
 */
public class TommyWrapperLifecycleListener implements TommyLifecycleListener {

    //监听器具体需要做的事情
    @Override
    public void lifecycleEvent(TommyLifecycleEvent event) {
        TommyLifecycle lifecycle = event.getLifecycle();

        System.out.println("Lehr's Wrapper Listener: "+event.getType());

        if(TommyLifecycle.START_EVENT.equalsIgnoreCase(event.getType()))
        {
            System.out.println("Starting wrapper......");
        }
        if(TommyLifecycle.STOP_EVENT.equalsIgnoreCase(event.getType()))
        {
            System.out.println("Stopping wrapper......");
        }

    }
}

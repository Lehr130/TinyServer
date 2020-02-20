package tiny.lehr.tomcat.lifecircle;

/**
 * @author Lehr
 * @create: 2020-02-19
 */
public class TommyContextLifecycleListener implements TommyLifecycleListener {

    //监听器具体需要做的事情
    @Override
    public void lifecycleEvent(TommyLifecycleEvent event) {
        TommyLifecycle lifecycle = event.getLifecycle();

        System.out.println("Lehr's Context Listener: "+event.getType());

        if(TommyLifecycle.START_EVENT.equalsIgnoreCase(event.getType()))
        {
            System.out.println("Starting context......");
        }
        if(TommyLifecycle.STOP_EVENT.equalsIgnoreCase(event.getType()))
        {
            System.out.println("Stopping context......");
        }

    }
}

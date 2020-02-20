package tiny.lehr.tomcat.lifecircle;

import java.util.EventObject;

/**
 * 生命周期事件
 */
public final class TommyLifecycleEvent extends EventObject {


    public TommyLifecycleEvent(TommyLifecycle lifecycle, String type) {

        this(lifecycle, type, null);

    }

    public TommyLifecycleEvent(TommyLifecycle lifecycle, String type, Object data) {

        super(lifecycle);
        this.lifecycle = lifecycle;
        this.type = type;
        this.data = data;

    }

    private Object data = null;

    private TommyLifecycle lifecycle = null;

    private String type = null;

    // ------------------------------------------------------------- Properties

    public Object getData() {

        return (this.data);

    }

    public TommyLifecycle getLifecycle() {

        return (this.lifecycle);

    }

    public String getType() {

        return (this.type);

    }

}

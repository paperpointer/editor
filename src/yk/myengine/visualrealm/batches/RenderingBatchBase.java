/**
 * File RenderingBatchBase.java
 * @author Yuri Kravchik
 * Created 17 01 2008
 */
package yk.myengine.visualrealm.batches;

import yk.myengine.optiseq.AbstractState;

/**
 * RenderingBatchBase
 *
 * @author Yuri Kravchik
 *         Created 17 01 2008
 */
abstract public class RenderingBatchBase {
    abstract public void init();

    abstract public void makeRelease();

    abstract public void draw();

    public boolean release = false;

    /**
     * Light
     */
    //FIXME light must have more abstract form. batches could implement lighting in other
    //than light state manner (for example with material shaders)
    protected AbstractState lightState;

    public void askRelease() {
        release = true;
    }

    public void setLight(AbstractState light) {
        this.lightState = light;
    }


}

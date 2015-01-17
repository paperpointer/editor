package yk.myengine.optiseq;

/**
 * Created by: Yuri Kravchik
 * Date: 30/10/2007
 * Time: 18:43:56
 */
abstract public class AbstractState implements State {
    public float cost() {
        return 0;
    }

    public void release() {
    }
}

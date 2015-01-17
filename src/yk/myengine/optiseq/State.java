package yk.myengine.optiseq;

/**
 * Created by: Yuri Kravchik
 * Date: 30/10/2007
 * Time: 17:38:34
 */
public interface State {
    public void enable();

    public void disable();

    public float cost();

    public void release();
}

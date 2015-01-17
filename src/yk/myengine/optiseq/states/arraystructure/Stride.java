package yk.myengine.optiseq.states.arraystructure;

/**
 * Created by: Yuri Kravchik
 * Date: 2/11/2007
 * Time: 10:38:13
 */
public abstract class Stride extends AbstractArrayStructure {
    protected int stride;

    public Stride(int stride) {
        this.stride = stride;
    }
}

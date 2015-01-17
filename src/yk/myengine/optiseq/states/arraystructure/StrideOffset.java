package yk.myengine.optiseq.states.arraystructure;

/**
 * Created by: Yuri Kravchik Date: 2/11/2007 Time: 10:28:41
 */
public abstract class StrideOffset extends Stride {
    /**
     * Specifies the byte stride from one attribute to the next, allowing
     * attribute values to be intermixed with other attribute values or stored
     * in a separate array.
     * <p/>
     * A value of 0 for stride means that the values are stored sequentially in
     * memory with no gaps between successive elements.
     */
    protected int offset;

    public StrideOffset(final int stride, final int offset) {
        super(stride);
        this.offset = offset;
    }
}

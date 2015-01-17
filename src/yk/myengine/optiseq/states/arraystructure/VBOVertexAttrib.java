package yk.myengine.optiseq.states.arraystructure;

import org.lwjgl.opengl.GL20;

/**
 * Created by: Yuri Kravchik Date: 2/11/2007 Time: 10:18:06
 */
public class VBOVertexAttrib extends StrideOffset {
    protected int index;
    /**
     * Specifies the number of components per attribute and must be 1, 2, 3, or
     * 4.
     */
    protected int size;
    /**
     * Specifies the data type of each component (GL_BYTE, GL_UNSIGNED_BYTE,
     * GL_SHORT, GL_UNSIGNED_SHORT, GL_INT, GL_UNSIGNED_INT, GL_FLOAT, or
     * GL_DOUBLE).
     */
    protected int type;
    /**
     * If set to GL_TRUE, normalize specifies that values stored in an integer
     * format are to be mapped to the range [1.0,1.0] (for signed values) or
     * [0.0,1.0] (for unsigned values) when they are accessed and converted to
     * floating point. Otherwise, values are converted to floats directly
     * without normalization. pointer is the memory address of the first generic
     * vertex attribute in the vertex array.
     */
    protected boolean normalized;

    /**
     * Specifies the byte stride from one attribute to the next, allowing
     * attribute values to be intermixed with other attribute values or stored
     * in a separate array.
     * <p/>
     * A value of 0 for stride means that the values are stored sequentially in
     * memory with no gaps between successive elements.
     */
//    protected int offset;

    public VBOVertexAttrib(final int index, final int size, final int type,
                           final boolean normalized, final int stride, final int offset) {
        super(stride, offset);
        this.index = index;
        this.type = type;
        this.normalized = normalized;
        this.size = size;

    }

    @Override
    public void turnOff() {
        GL20.glDisableVertexAttribArray(index);
    }

    @Override
    public void turnOn() {
        GL20.glEnableVertexAttribArray(index);
        GL20.glVertexAttribPointer(index, size, type, normalized, stride, offset);
    }
}

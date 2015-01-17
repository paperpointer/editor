package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import java.nio.ShortBuffer;
import java.util.List;

/**
 * Created by: Yuri Kravchik Date: 31/10/2007 Time: 18:17:39
 */
public class DrawIndicesShort extends AbstractState {
    private final ShortBuffer indexBuffer;
    /**
     * Specifies what kind of primitives to render. Symbolic constants
     * GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP,
     * GL_TRIANGLE_FAN, GL_TRIANGLES, GL_QUAD_STRIP, GL_QUADS, and GL_POLYGON
     * are accepted.
     */
    private final int primitiveType;

    public DrawIndicesShort(final int primitiveType, List<? extends Number> indices) {
        this.primitiveType = primitiveType;
        this.indexBuffer = BufferUtils.createShortBuffer(indices.size());
        for (Number aShort : indices) indexBuffer.put(aShort.shortValue());
        indexBuffer.rewind();
    }

    public void disable() {
    }

    public void enable() {
        GL12.glDrawRangeElements(primitiveType, 0, indexBuffer.limit(), indexBuffer);
    }

    public void draw() {
        enable();
    }

    @Override
    public void release() {
    }
}

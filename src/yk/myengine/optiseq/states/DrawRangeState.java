package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.opengl.GL12;

import java.nio.ShortBuffer;

/**
 * Created by: Yuri Kravchik Date: 31/10/2007 Time: 18:17:39
 */
public class DrawRangeState extends AbstractState {
    private final ShortBuffer indexBuffer;
    /**
     * Specifies what kind of primitives to render. Symbolic constants
     * GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP,
     * GL_TRIANGLE_FAN, GL_TRIANGLES, GL_QUAD_STRIP, GL_QUADS, and GL_POLYGON
     * are accepted.
     */
    private final int primitiveType;
    private final int from;
    private final int to;

    public DrawRangeState(final ShortBuffer indexBuffer, final int primitiveType, final int from,
                          final int to) {
        this.primitiveType = primitiveType;
        this.to = to;
        this.from = from;
        this.indexBuffer = indexBuffer;
    }

    public void disable() {
    }

    public void enable() {
        GL12.glDrawRangeElements(primitiveType, from, to, indexBuffer);
    }

    @Override
    public void release() {
    }
}

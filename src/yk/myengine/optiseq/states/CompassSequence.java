package yk.myengine.optiseq.states;

import yk.myengine.data.geometry.bufferstructure.StructureBundleTight;
import yk.myengine.data.geometry.bufferstructure.StructureUnitVertex3f;
import yk.myengine.optiseq.AbstractState;
import yk.myengine.optiseq.Sequence;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * Created by: Yuri Kravchik Date: 31/10/2007 Time: 17:22:46
 */
public class CompassSequence extends AbstractState {
    private final Sequence sequence = new Sequence();

    public CompassSequence() {

        final StructureUnitVertex3f vertices = new StructureUnitVertex3f();

        final StructureBundleTight bf = new StructureBundleTight(vertices);
        final ByteBuffer bBuffer = BufferUtils.createByteBuffer(9 * bf.stride);
        final ShortBuffer iBuffer = BufferUtils.createShortBuffer(9);
        bf.setBuffer(bBuffer);

        vertices.set(0, 5, 0, 0);
        vertices.set(1, 0, 0, 20);
        vertices.set(2, 0, 0, 0);

        vertices.set(3, 0, 0, 0);
        vertices.set(4, 0, 4, 0);
        vertices.set(5, 12, 0, 0);

        vertices.set(6, 0, 0, 0);
        vertices.set(7, 0, 0, 4);
        vertices.set(8, 0, 8, 0);

        iBuffer.put(0, (short) 0);
        iBuffer.put(1, (short) 1);
        iBuffer.put(2, (short) 2);

        iBuffer.put(3, (short) 3);
        iBuffer.put(4, (short) 4);
        iBuffer.put(5, (short) 5);

        iBuffer.put(6, (short) 6);
        iBuffer.put(7, (short) 7);
        iBuffer.put(8, (short) 8);

        sequence.getStates().add(new DataBufferState(bBuffer, 0, true));
        sequence.getStates().add(new VertexStructureState(bf, null));
        sequence.getStates().add(new ColorState3b((byte) 50, (byte) 250, (byte) 50));
        sequence.getStates()
                .add(new DrawRangeState(iBuffer, GL11.GL_TRIANGLES, 0, iBuffer.limit()));
    }

    public void disable() {
        sequence.disable();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    public void enable() {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        sequence.enable();
    }
}

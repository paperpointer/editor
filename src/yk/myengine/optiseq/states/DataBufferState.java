package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by: Yuri Kravchik Date: 26/10/2007 Time: 10:54:32
 */
public class DataBufferState extends AbstractState {
    public static boolean isArbVboSupported() {
        return GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
    }

    private final IntBuffer intBuffer1 = BufferUtils.createIntBuffer(1);
    private final int dataBufferID;
    private int usage;
    //    private VertexStructureState vertexStructureState;
    private ByteBuffer bBuffer;

    private final boolean vboEnabled;

    public DataBufferState(final ByteBuffer data, final int usage, final boolean vboEnabled) {
        if (vboEnabled && !isArbVboSupported()) {
            throw new Error("ARBVBO is not supported, but \"" + this.getClass().getName()
                    + "\" constructor is called!");
        }
        dataBufferID = createVBOID();
        //!!!!
        this.usage = usage;
        this.usage = ARBBufferObject.GL_DYNAMIC_DRAW_ARB;
        this.vboEnabled = vboEnabled;
        if (vboEnabled) {
            bufferData(data);
        } else {
            bBuffer = data;
        }
//        vertexStructureState = new VertexStructureState(units, shaderHandler);
    }

    private int createVBOID() {
        ARBBufferObject.glGenBuffersARB(intBuffer1);
        return intBuffer1.get(0);
    }

    public void bufferData(final ByteBuffer buffer) {
        ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, dataBufferID);
        ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, usage);
        disable();
    }

    public void disable() {
//        vertexStructureState.disable();
        ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
    }

    public void enable() {
        if (vboEnabled) {
            ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, dataBufferID);
        } else {

        }
    }

    @Override
    public void release() {
        ARBBufferObject.glDeleteBuffersARB(intBuffer1);
    }
}

package yk.myengine.optiseq.states.shaders;

import yk.jcommon.fastgeom.Matrix4;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 06/01/15
 * Time: 13:33
 */
public class UniformMatrix4 extends UniformVariable {
    public FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);

    public UniformMatrix4(String name) {
        this.name = name;
    }

    public void set(Matrix4 m) {
        m.store(matrixBuffer);
        matrixBuffer.rewind();
    }

    @Override
    public void plug() {
        GL20.glUniformMatrix4(index, false, matrixBuffer);
    }
}

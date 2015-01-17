package yk.myengine.optiseq.states.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by: Yuri Kravchik Date: 2 11 2007 Time: 16:25:05
 */
public class Uniform3f extends UniformVariable {
    public Vector3f v3f = new Vector3f();

    public Uniform3f(final String name, final float a, final float b, final float c) {
        this.name = name;
        v3f.x = a;
        v3f.y = b;
        v3f.z = c;
    }

    @Override
    public void plug() {
        GL20.glUniform3f(index, v3f.x, v3f.y, v3f.z);
    }
}

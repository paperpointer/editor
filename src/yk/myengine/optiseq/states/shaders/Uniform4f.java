package yk.myengine.optiseq.states.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Quaternion;

/**
 * Created by: Yuri Kravchik Date: 2 11 2007 Time: 16:09:33
 */
public class Uniform4f extends UniformVariable {
    public Quaternion q = new Quaternion();

    public Uniform4f(final String name, final float a, final float b, final float c, final float d) {
        this.name = name;
        q.w = a;
        q.x = b;
        q.y = c;
        q.z = d;
    }

    @Override
    public void plug() {
        GL20.glUniform4f(index, q.w, q.x, q.y, q.z);
    }
}

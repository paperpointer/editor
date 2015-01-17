package yk.myengine.optiseq.states.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by: Yuri Kravchik Date: 2 11 2007 Time: 16:25:36
 */
public class Uniform2f extends UniformVariable {
    public Vector2f v2f = new Vector2f();

    public Uniform2f(final String name, final float a, final float b) {
        this.name = name;
        v2f.x = a;
        v2f.y = b;
    }

    @Override
    public void plug() {
        GL20.glUniform2f(index, v2f.x, v2f.y);
    }
}

/**
 * File Uniform1f.java
 * @author Yuri Kravchik
 * Created 16.05.2008
 */
package yk.myengine.optiseq.states.shaders;

import org.lwjgl.opengl.GL20;

/**
 * Uniform1f
 *
 * @author Yuri Kravchik Created 16.05.2008
 */
public class Uniform1f extends UniformVariable {
    public float value;

    public Uniform1f(final String name, final float value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void plug() {
        GL20.glUniform1f(index, value);
    }

}

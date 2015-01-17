/**
 * File Uniform1Af.java
 * @author Yuri Kravchik
 * Created 17.05.2008
 */
package yk.myengine.optiseq.states.shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

/**
 * Uniform1Af
 *
 * @author Yuri Kravchik Created 17.05.2008
 */
public class Uniform1Af extends UniformVariable {
    private final FloatBuffer values;

    public Uniform1Af(final String name, final Float... values) {
        this.values = BufferUtils.createFloatBuffer(values.length);
        for (final Float i : values) {
            this.values.put(i);
        }
        this.values.rewind();
        this.name = name;
    }

    @Override
    public void plug() {
        GL20.glUniform1(index, values);
    }

}

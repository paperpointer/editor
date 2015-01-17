/**
 * File UniformAi.java
 * @author Yuri Kravchik
 * Created 16.05.2008
 */
package yk.myengine.optiseq.states.shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.IntBuffer;

/**
 * UniformAi
 *
 * @author Yuri Kravchik Created 16.05.2008
 */
public class Uniform4Ai extends UniformVariable {
    private final IntBuffer values;

    public Uniform4Ai(final String name, final Integer... values) {
        this.values = BufferUtils.createIntBuffer(values.length);
        for (final Integer i : values) {
            this.values.put(i);
        }
        this.values.rewind();
        this.name = name;
    }

    @Override
    public void plug() {
        GL20.glUniform4(index, values);
    }
}

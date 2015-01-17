package yk.myengine.optiseq.states.shaders;

import org.lwjgl.opengl.GL20;

/**
 * Created by: Yuri Kravchik Date: 2 11 2007 Time: 16:10:19
 */
abstract public class UniformVariable {
    public static UniformVariable createVariable(final String name, final float a, final float b) {
        return new Uniform2f(name, a, b);
    }

    public static UniformVariable createVariable(final String name, final float a, final float b,
                                                 final float c) {
        return new Uniform3f(name, a, b, c);
    }

    public static UniformVariable createVariable(final String name, final float a, final float b,
                                                 final float c, final float d) {
        return new Uniform4f(name, a, b, c, d);
    }

    public static UniformVariable createVariable(final String name, final int value) {
        return new Uniform1i(name, value);
    }

    public String name;

    public int index;

    public void initForProgram(final int program) {
        index = GL20.glGetUniformLocation(program, ShaderHandler.getBufferedString(name));
    }

    abstract public void plug();
}

package yk.myengine.optiseq.states.shaders;

import org.lwjgl.opengl.GL20;

/**
 * Copyright Yuri Kravchik 2007 Created by Yuri Kravchik Date: 09.12.2007 Time:
 * 15:30:43
 */
public class Uniform1i extends UniformVariable {
    private final int value;

    public Uniform1i(final String name, final int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void plug() {
        GL20.glUniform1i(index, value);
    }
}

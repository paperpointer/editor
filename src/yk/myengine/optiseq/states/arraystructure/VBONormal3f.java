package yk.myengine.optiseq.states.arraystructure;

import org.lwjgl.opengl.GL11;

/**
 * Copyright Yuri Kravchik 2007 Date: 02.11.2007 Time: 23:54:11
 */
public class VBONormal3f extends StrideOffset {

    public VBONormal3f(int stride, int offset) {
        super(stride, offset);
    }

    @Override
    public void turnOff() {
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    }

    @Override
    public void turnOn() {
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glNormalPointer(GL11.GL_FLOAT, stride, offset);
    }
}

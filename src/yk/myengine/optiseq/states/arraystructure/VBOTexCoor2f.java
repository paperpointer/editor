package yk.myengine.optiseq.states.arraystructure;

import org.lwjgl.opengl.GL11;

/**
 * Created by: Yuri Kravchik Date: 5 11 2007 Time: 17:55:24
 */
public class VBOTexCoor2f extends StrideOffset {
    public VBOTexCoor2f(int stride, int offset) {
        super(stride, offset);
    }

    @Override
    public void turnOff() {
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    @Override
    public void turnOn() {
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, offset);
    }
}

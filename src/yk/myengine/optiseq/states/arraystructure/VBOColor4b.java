package yk.myengine.optiseq.states.arraystructure;

import org.lwjgl.opengl.GL11;

/**
 * Created by: Yuri Kravchik Date: 5 11 2007 Time: 13:40:52
 */
public class VBOColor4b extends StrideOffset {

    public VBOColor4b(int stride, int offset) {
        super(stride, offset);
    }

    @Override
    public void turnOff() {
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

    @Override
    public void turnOn() {
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, stride, offset);
    }
}

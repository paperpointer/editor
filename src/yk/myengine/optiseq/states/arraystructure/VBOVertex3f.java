package yk.myengine.optiseq.states.arraystructure;

import org.lwjgl.opengl.GL11;

/**
 * Created by: Yuri Kravchik Date: 2/11/2007 Time: 10:12:51
 */
public class VBOVertex3f extends StrideOffset {

    public VBOVertex3f(int stride, int offset) {
        super(stride, offset);
    }

    @Override
    public void turnOff() {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    @Override
    public void turnOn() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, offset);
    }
}

package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.opengl.GL11;

/**
 * Created by: Yuri Kravchik
 * Date: 31/10/2007
 * Time: 10:24:23
 */
public class ZRotator extends AbstractState {
    private float angle;

    public void enable() {
        GL11.glPushMatrix();
        GL11.glRotatef(angle, 0, 0, 1.0f);
    }

    public void disable() {
        GL11.glPopMatrix();
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}

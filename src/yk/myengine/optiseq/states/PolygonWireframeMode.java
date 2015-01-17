package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.opengl.GL11;

/**
 * Created by: Yuri Kravchik
 * Date: 5 11 2007
 * Time: 18:36:38
 */
public class PolygonWireframeMode extends AbstractState {
    public void enable() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
    }

    public void disable() {
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
    }
}

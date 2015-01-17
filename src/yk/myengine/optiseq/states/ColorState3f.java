package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by: Yuri Kravchik
 * Date: 2 11 2007
 * Time: 11:59:49
 */
public class ColorState3f extends AbstractState {
    public Vector3f v3f = new Vector3f();

    public ColorState3f(float r, float g, float b) {
        v3f.x = r;
        v3f.y = g;
        v3f.z = b;
    }

    public void enable() {
        GL11.glColor3f(v3f.x, v3f.y, v3f.z);
    }

    public void disable() {
    }
}

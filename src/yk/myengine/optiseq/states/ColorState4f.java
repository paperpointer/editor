package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by: Yuri Kravchik
 * Date: 2 11 2007
 * Time: 11:59:49
 */
public class ColorState4f extends AbstractState {
    public Vector4f v4f = new Vector4f();

    public ColorState4f(float r, float g, float b, float a) {
        v4f.x = r;
        v4f.y = g;
        v4f.z = b;
        v4f.w = a;
    }

    public void enable() {
        GL11.glColor4f(v4f.x, v4f.y, v4f.z, v4f.w);
    }

    public void disable() {
    }
}

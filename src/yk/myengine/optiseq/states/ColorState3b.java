package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.opengl.GL11;

/**
 * Created by: Yuri Kravchik
 * Date: 2 11 2007
 * Time: 11:59:49
 */
public class ColorState3b extends AbstractState {
    public byte a, b, c;

    public ColorState3b(byte a, byte b, byte c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void enable() {
        GL11.glColor3b(a, b, c);
    }

    public void disable() {
    }
}

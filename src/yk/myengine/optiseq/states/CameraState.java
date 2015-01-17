package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Created by: Yuri Kravchik Date: 30/10/2007 Time: 15:30:54
 */
public class CameraState extends AbstractState {
    private float near = 10;
    private float far = 40000;
    private float aspect =
            (float) Display.getDisplayMode().getWidth() / Display.getDisplayMode().getHeight();
    private float fovy = 45;

    public CameraState() {

    }

    public CameraState(float fovy, float aspect, float near, float far) {
        this.fovy = fovy;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
    }

    public void disable() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    public void enable() {
//todo do it only once per frame?
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
//        GL11.glOrtho(-1000, 1000, -1000, 1000, -1, 1);
//        GL11.glTranslated(-1, -1, 0);
        GLU.gluPerspective(fovy, aspect, near, far);
//        GLU.gluLookAt(0, 0, 1, 0, 0, 0, 0, 1, 0);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
//        GL11.glScalef(1, 1, -1);
//        GL11.glTranslatef(0, 0, -100);
//        GL11.glTranslatef(Display.getDisplayMode().getWidth() / 2, Display.getDisplayMode().getHeight() / 2, 0.0f);

    }
}

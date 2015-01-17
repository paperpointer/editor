package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

/**
 * Created by: Yuri Kravchik Date: 6 11 2007 Time: 13:12:37
 */
public class SimplestLightState extends AbstractState {
    private FloatBuffer lightAmbientColor;
    private FloatBuffer lightPosition;
    public FloatBuffer lightColor;

    public SimplestLightState() {
        lightColor = BufferUtils.createFloatBuffer(4);
        lightColor.put(0, 1.0f);
        lightColor.put(1, 1.0f);
        lightColor.put(2, 1.0f);
        lightColor.put(3, 1.0f);

        lightAmbientColor = BufferUtils.createFloatBuffer(4);
        lightAmbientColor.put(0, 0.0f);
        lightAmbientColor.put(1, 0.0f);
        lightAmbientColor.put(2, 0.0f);
        lightAmbientColor.put(3, 0.2f);

        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0, 1);
        lightPosition.put(1, 1);
        lightPosition.put(2, 1);
        lightPosition.put(3, 0);
    }

    public void disable() {
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        GL11.glDisable(GL11.GL_LIGHT0);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public void enable() {//intensity, emission
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, lightAmbientColor);
//        GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, lightAmbientColor);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightColor);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, lightColor);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
//        GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
//        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, lightColor);
//        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, lightColor);
    }
}

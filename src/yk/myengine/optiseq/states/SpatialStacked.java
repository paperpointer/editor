package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by: Yuri Kravchik
 * Date: 30/10/2007
 * Time: 15:18:24
 */
public class SpatialStacked extends AbstractState {
    private Matrix4f transform;
    private FloatBuffer bMatrix = BufferUtils.createFloatBuffer(16);

    public SpatialStacked() {
        setTransform(new Matrix4f());
//        bMatrix.put(1);bMatrix.put(0);bMatrix.put(0);bMatrix.put(0);
//        bMatrix.put(0);bMatrix.put(1);bMatrix.put(0);bMatrix.put(0);
//        bMatrix.put(0);bMatrix.put(0);bMatrix.put(1);bMatrix.put(0);
//        bMatrix.put(0);bMatrix.put(0);bMatrix.put(0);bMatrix.put(1);
//        bMatrix.rewind();
    }

    public void enable() {
        GL11.glPushMatrix();
        GL11.glMultMatrix(bMatrix);
    }

    public void disable() {
        GL11.glPopMatrix();
    }

    public void setTransform(Matrix4f transform) {
        this.transform = transform;
        transform.store(bMatrix);
        bMatrix.rewind();
    }

    public void setTransform(FloatBuffer fBuffer) {
        this.transform.load(fBuffer);
        bMatrix.put(fBuffer);
        bMatrix.rewind();
    }

    public void translate(Vector3f position) {
        transform.translate(new org.lwjgl.util.vector.Vector3f(position.x, position.y, position.z));
        setTransform(transform);
    }
}

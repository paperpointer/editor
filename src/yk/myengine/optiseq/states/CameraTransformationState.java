package yk.myengine.optiseq.states;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by: Yuri Kravchik Date: 31/10/2007 Time: 13:30:30
 */
public class CameraTransformationState extends AbstractState {
    private float moveSpeed = 1f;
    private final float rotateSpeed = 1f;
    private float rotH = 0;
    private float rotV = 0;
    private final Matrix4f mat4f = new Matrix4f(), m1 = new Matrix4f(), m2 = new Matrix4f();
    private final Vector4f strifeVec = new Vector4f();
    private final Vector4f stepVec = new Vector4f();
    private final Vector4f strifeVecBase = new Vector4f(1, 0, 0, 0);
    private final Vector4f stepVecBase = new Vector4f(0, 0, 1, 0);
    private final Vector3f position = new Vector3f(0, 0, -100);
    private CompassSequence compass = null;

    public CameraTransformationState() {
        mat4f.setIdentity();
        refreshMatrix();
    }

    private void refreshMatrix() {
        m1.setIdentity();
        m2.setIdentity();
        m1.rotate((float) (-rotH / 180 * Math.PI), new Vector3f(1, 0, 0));//TODO constant or use MY VECTOR
        m2.rotate((float) ((-rotV) / 180 * Math.PI), new Vector3f(0, 1, 0));

        mat4f.setIdentity();
        Matrix4f.mul(mat4f, m2, mat4f);
        Matrix4f.mul(mat4f, m1, mat4f);

        Matrix4f.transform(mat4f, strifeVecBase, strifeVec);
        Matrix4f.transform(mat4f, stepVecBase, stepVec);

        strifeVec.scale(moveSpeed / 1.5f);
        stepVec.scale(moveSpeed);
    }

    public void disable() {
        GL11.glPopMatrix();

        if (compass == null) {
            compass = new CompassSequence();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(-50, -50, -200);
        GL11.glRotatef(rotH, 1, 0, 0);
        GL11.glRotatef(rotV, 0, 1, 0);
        compass.enable();
        compass.disable();
        GL11.glPopMatrix();
    }

    public void down() {
        position.y += moveSpeed;
    }

    public void enable() {
        GL11.glPushMatrix();
        GL11.glRotatef(rotH, 1, 0, 0);
        GL11.glRotatef(rotV, 0, 1, 0);
        GL11.glTranslatef(position.x, position.y, position.z);
    }

    public void getPosition(final Vector3f result) {
        result.set(-position.x, -position.y, -position.z);
    }

    public float getRotH() {
        return rotH;
    }

    public float getRotV() {
        return rotV;
    }

    public void rotAH() {
        rotH -= rotateSpeed;
        if (rotH < -90) {
            rotH = -90;
        }
        refreshMatrix();
    }

    public void rotAV() {
        rotV -= rotateSpeed;
        rotV %= 360;
        refreshMatrix();
    }

    public void rotH() {
        rotH += rotateSpeed;
        if (rotH > 90) {
            rotH = 90;
        }
        refreshMatrix();
    }

    public void rotV() {
        rotV += rotateSpeed;
        rotV %= 360;
        refreshMatrix();
    }

    /**
     * @param moveSpeed the moveSpeed to set
     */
    public void setMoveSpeed(final float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void stepBackward() {
        position.x -= stepVec.x;
        position.y -= stepVec.y;
        position.z -= stepVec.z;
    }

    public void stepForward() {
        position.x += stepVec.x;
        position.y += stepVec.y;
        position.z += stepVec.z;
    }

    public void strifeLeft() {
        position.x += strifeVec.x;
        position.y += strifeVec.y;
        position.z += strifeVec.z;
    }

    public void strifeRight() {
        position.x -= strifeVec.x;
        position.y -= strifeVec.y;
        position.z -= strifeVec.z;
    }

    public void up() {
        position.y -= moveSpeed;
    }
}

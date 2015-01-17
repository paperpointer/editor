/**
 * File DebugTriangle.java
 * @author Yuri Kravchik
 * Created 17 01 2008
 */
package yk.myengine.data.geometry.debugdata;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * DebugTriangle
 *
 * @author Yuri Kravchik Created 17 01 2008
 */
public class DebugTriangle {
    public Vector3f[] coords;
    public Vector4f color;
    public boolean visible = true;

    public DebugTriangle() {
        this.coords = new Vector3f[3];
        coords[0] = new Vector3f();
        coords[1] = new Vector3f();
        coords[2] = new Vector3f();
        color = new Vector4f();
    }

    public DebugTriangle(Vector3f coords1, Vector3f coords2, Vector3f coords3, Vector4f color) {
        this();
        set(coords1, coords2, coords3, color);
    }

    public void set(Vector3f coords1, Vector3f coords2, Vector3f coords3, Vector4f color) {
        coords[0] = coords1;
        coords[1] = coords2;
        coords[2] = coords3;
        this.color = color;
    }

}

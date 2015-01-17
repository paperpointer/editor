/**
 * File DebugPoint.java
 * @author Yuri Kravchik
 * Created 17 01 2008
 */
package yk.myengine.data.geometry.debugdata;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * DebugPoint
 *
 * @author Yuri Kravchik Created 17 01 2008
 */
public class DebugPoint {
    public Vector3f coords;
    public Vector4f color;
    public boolean visible = true;

    public DebugPoint() {
        coords = new Vector3f();
        color = new Vector4f();
    }

    public DebugPoint(Vector3f coords, Vector4f color) {
        set(coords, color);
    }

    public void set(Vector3f coords, Vector4f color) {
        this.coords = coords;
        this.color = color;
    }
}

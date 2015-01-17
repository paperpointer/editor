/**
 * File DebugLine.java
 * @author Yuri Kravchik
 * Created 17 01 2008
 */
package yk.myengine.data.geometry.debugdata;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * DebugLine
 *
 * @author Yuri Kravchik Created 17 01 2008
 */
public class DebugLine {
    public Vector3f[] coords;
    public Vector4f color;
    public boolean visible = true;

    public DebugLine() {
        this.coords = new Vector3f[2];
        coords[0] = new Vector3f();
        coords[1] = new Vector3f();
        color = new Vector4f();
    }

    public DebugLine(Vector3f coords1, Vector3f coords2, Vector4f color) {
        this();
        set(coords1, coords2, color);
    }

    public void set(Vector3f coords1, Vector3f coords2, Vector4f color) {
        coords[0] = coords1;
        coords[1] = coords2;
        this.color = color;
    }
}

/**
 * File DebugInfo.java
 * @author Yuri Kravchik
 * Created 17 01 2008
 */
package yk.myengine.data.geometry.debugdata;

import java.util.ArrayList;
import java.util.List;

/**
 * DebugInfo
 *
 * @author Yuri Kravchik
 *         Created 17 01 2008
 */
public class DebugInfo {
    public List<DebugPoint> points = new ArrayList<DebugPoint>();
    public List<DebugLine> lines = new ArrayList<DebugLine>();
    public List<DebugTriangle> triangles = new ArrayList<DebugTriangle>();
}

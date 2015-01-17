/**
 * File DebugInfoBatch.java
 * @author Yuri Kravchik
 * Created 17 01 2008
 */
package yk.myengine.visualrealm.batches;

import yk.myengine.data.geometry.debugdata.DebugInfo;
import yk.myengine.data.geometry.debugdata.DebugLine;
import yk.myengine.data.geometry.debugdata.DebugPoint;
import yk.myengine.data.geometry.debugdata.DebugTriangle;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * DebugInfoBatch
 *
 * @author Yuri Kravchik Created 17 01 2008
 */
public class DebugInfoBatch extends RenderingBatchBase {
    private static void drawLines(final List<DebugLine> lines) {
        GL11.glBegin(GL11.GL_LINES);
        for (final DebugLine point : lines) {
            if (point.visible) {
                GL11.glColor3f(point.color.x, point.color.y, point.color.z);
                GL11.glVertex3f(point.coords[0].x, point.coords[0].y, point.coords[0].z);
                GL11.glVertex3f(point.coords[1].x, point.coords[1].y, point.coords[1].z);
            }
        }
        GL11.glEnd();
    }

    private static void drawPoints(final List<DebugPoint> points) {
        GL11.glBegin(GL11.GL_POINTS);
        for (final DebugPoint point : points) {
            if (point.visible) {
                GL11.glColor3f(point.color.x, point.color.y, point.color.z);
                GL11.glVertex3f(point.coords.x, point.coords.y, point.coords.z);
            }
        }
        GL11.glEnd();
    }

    private DebugInfo debugInfo;
    private int triangleDrawing = GL11.GL_TRIANGLES;

    public DebugInfoBatch() {
    }

    public DebugInfoBatch(final DebugInfo debugInfo) {
        setDebugInfo(debugInfo);
    }

    private void drawTriangles(final List<DebugTriangle> triangles) {
        GL11.glBegin(triangleDrawing);
        for (final DebugTriangle point : triangles) {
            if (point.visible) {
                GL11.glColor3f(point.color.x, point.color.y, point.color.z);
                GL11.glVertex3f(point.coords[0].x, point.coords[0].y, point.coords[0].z);
                GL11.glVertex3f(point.coords[1].x, point.coords[1].y, point.coords[1].z);
                GL11.glVertex3f(point.coords[2].x, point.coords[2].y, point.coords[2].z);
            }
        }
        GL11.glEnd();
    }

    @Override
    public void draw() {
        drawPoints(debugInfo.points);
        drawLines(debugInfo.lines);
        drawTriangles(debugInfo.triangles);
    }

    @Override
    public void init() {
    }

    @Override
    public void makeRelease() {
    }

    public void setDebugInfo(final DebugInfo debugInfo) {
        this.debugInfo = debugInfo;
    }

    public void setTriangleDrawing(final int triangleDrawing) {
        this.triangleDrawing = triangleDrawing;
    }
}

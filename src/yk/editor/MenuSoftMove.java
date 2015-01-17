package yk.editor;

import yk.jcommon.fastgeom.Quaternionf;
import yk.jcommon.fastgeom.Vec3f;

import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;
import static yk.jcommon.utils.Util.list;
import static yk.jcommon.utils.Util.set;


/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/6/14
 * Time: 12:04 AM
 */
public class MenuSoftMove extends MenuEntry {
    public float size;
    public boolean hard;

    //TODO fix this
    static private Vec3f startBrushPos;
    private Quaternionf startBrushRot;
    private List<Vec3f> startVertexPos = list();
    private List<Vec3f> startVertexNorm = list();

    public MenuSoftMove(String name, float size, boolean hard) {
        super(name);
        this.size = size;
        this.hard = hard;
    }

    @Override
    public void press(DDDEditor editor) {
        if (startBrushPos != null) return;
        startBrushPos = editor.arrowSceneSpacePos;
        startBrushRot = editor.arrowSceneSpaceRot;
        startVertexPos = list();
        startVertexNorm = list();
        for (Vertex v : editor.vertices) {
            startVertexPos.add(v.pos);
            startVertexNorm.add(v.normal);
        }
    }


    @Override
    public void tick(DDDEditor editor) {
        float size = this.size * editor.magnifier;
        if (startBrushPos == null) select(editor, size);
        if (startBrushRot == null) return;
        Vec3f d = editor.arrowSceneSpacePos.sub(startBrushPos);
        for (int i = 0; i < startVertexPos.size(); i++) {
            if (!editor.vertices.get(i).selected) continue;
            Vec3f startVec = startVertexPos.get(i);
            float dist = startBrushPos.dist(startVec);
            float power = Math.min(dist, abs(size)) / abs(size);
            if (hard && power < 1) power = 0;
            if (power >= 1) continue;
            power *= power;
            //close is 1, far is 0
            power = Math.signum(size) * (1 - power);//power -1 : is magnifier

            Vec3f rotated = startBrushRot.nlerp(editor.arrowSceneSpaceRot, power).rotSub(startBrushRot).rotateFast(startVec.sub(startBrushPos)).add(startBrushPos);
            editor.vertices.get(i).pos = rotated.add(d.mul(power));

            //editor.vertices.get(i).normal = startBrushRot.nlerp(editor.arrowSceneSpaceRot, power).rotSub(startBrushRot).rotateFast(startVertexNorm.get(i));
        }
        editor.fixNormals(true);
    }

    public void select(final DDDEditor editor, float size) {
        Set<Vertex> all = set();
        Vertex closest = null;
        for (Vertex vertex : editor.vertices) {
            vertex.selected = false;
            if (editor.arrowSceneSpacePos.dist(vertex.pos) < abs(size)) {
                if (closest == null || editor.arrowSceneSpacePos.dist(vertex.pos) < editor.arrowSceneSpacePos.dist(closest.pos)) {
                    closest = vertex;
                }
                all.add(vertex);
            }
        }
        if (closest == null) return;
        closest.selected = true;
        boolean wasAdded = true;
        while (wasAdded) {
            wasAdded = false;
            for (Polygon polygon : editor.polygons) {
                if (polygon.isSelected()) {
                    for (Vertex v : polygon.vertices) {
                        if (!v.selected && all.contains(v)) {
                            v.selected = true;
                            wasAdded = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void release(DDDEditor editor) {
        editor.makeHistory();
        if (startBrushRot == null) return;
        startBrushPos = null;
        startBrushRot = null;
        startVertexNorm = null;
    }
}

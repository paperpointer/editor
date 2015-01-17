package yk.editor;

import yk.jcommon.fastgeom.Quaternionf;
import yk.jcommon.fastgeom.Vec3f;

import static java.lang.Math.abs;


/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/6/14
 * Time: 12:04 AM
 */
public class MenuSoftLens extends MenuEntry {
    public float size;
    public boolean hard;

    //TODO fix this
    private Quaternionf startBrushRot;

    public MenuSoftLens(String name, float size, boolean hard) {
        super(name);
        this.size = size;
        this.hard = hard;
    }

    @Override
    public void press(DDDEditor editor) {
        startBrushRot = editor.arrowSceneSpaceRot;
    }

    @Override
    public void tick(DDDEditor editor) {
        if (!pressed) return;
        float size = this.size * editor.magnifier;
//        if (startBrushPos == null) for (Vertex vertex : editor.vertices) vertex.selected = editor.arrowSceneSpacePos.dist(vertex.pos) < abs(size);
        if (startBrushRot == null) return;
//        Vec3f d = editor.arrowSceneSpacePos.sub(startBrushPos);
        for (int i = 0; i < editor.vertices.size(); i++) {
            Vec3f startVec = editor.vertices.get(i).pos;
            float dist = editor.arrowSceneSpacePos.dist(startVec);
            float power = Math.min(dist, abs(size)) / abs(size);
            if (hard && power < 1) power = 0;
            power *= power;
            //close is 1, far is 0
            power = Math.signum(size) * (1 - power);//power -1 : is magnifier

            power *= editor.dt / 10;
            //TODO auto normals
//            Vec3f rotated = startBrushRot.nlerp(editor.arrowSceneSpaceRot, power).rotSub(startBrushRot).rotateFast(startVec.sub(startBrushPos)).add(startBrushPos);
//            editor.vertices.get(i).pos = rotated.add(d.mul(power));
            editor.vertices.get(i).pos = editor.arrowSceneSpacePos.sub(startVec).mul(power).add(startVec);

//            editor.vertices.get(i).normal = startBrushRot.nlerp(editor.arrowSceneSpaceRot, power).rotSub(startBrushRot).rotateFast(editor.vertices.get(i).normal);
        }
    }

    @Override
    public void release(DDDEditor editor) {
        editor.makeHistory();
        if (startBrushRot == null) return;
        startBrushRot = null;
    }
}

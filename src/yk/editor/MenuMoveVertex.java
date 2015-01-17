package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

import java.util.List;

import static yk.jcommon.utils.Util.list;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 7/4/13
 * Time: 12:55 AM
 */
public class MenuMoveVertex extends MenuEntry {
    private Vec3f startBrushPos;
    private List<Vec3f> startVertexPos = list();
    private List<Vertex> selectedVertices = list();

    public MenuMoveVertex(String key, String name) {
        super(name);
    }


    @Override
    public void press(DDDEditor editor) {
        startBrushPos = editor.arrowSceneSpacePos;
        startVertexPos = list();
        selectedVertices = list();
        for (Vertex v : editor.vertices) {
            if (v.pos.dist(startBrushPos) < 2) {
                startVertexPos.add(v.pos);
                selectedVertices.add(v);
            }
        }
    }

    @Override
    public void tick(DDDEditor editor) {
        if (!selectedVertices.isEmpty()) {
            Vec3f d = editor.arrowSceneSpacePos.sub(startBrushPos);
            for (int i = 0; i < startVertexPos.size(); i++) {
                Vec3f start = startVertexPos.get(i);
                selectedVertices.get(i).pos = start.add(d);
            }
        }
    }

    @Override
    public void release(DDDEditor editor) {
        Vertex other = null;
        for (Vertex v : editor.vertices) {
            if (v.pos.dist(editor.arrowSceneSpacePos) < 2 && !selectedVertices.contains(v)) {
                other = v;
                break;
            }
        }
        if (other != null) for (Vertex selected : selectedVertices) editor.replace(selected, other);
        //TODO destroy degenerated triangle
        selectedVertices = list();
    }


}

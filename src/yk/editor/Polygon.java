package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static yk.jcommon.utils.Util.list;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 7/3/13
 * Time: 11:21 PM
 */
public class Polygon {
    public Vec3f normal;

    public final List<Vertex> vertices = list();

    public Polygon(Vertex... ii) {
        Collections.addAll(vertices, ii);
    }

    public boolean isSelected() {
        for (Vertex v : vertices) if (v.selected) return true;
        return false;
    }

    public Polygon copy(Map<Vertex, Vertex> mm) {//TODO get rid, make immutable
        Polygon result = new Polygon();
        result.normal = normal;
        for (Vertex v : vertices) result.vertices.add(mm.get(v));
        return result;
    }
}

package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 7/3/13
 * Time: 11:20 PM
 */
public class Vertex {
    //TODO fix hack
    public boolean dirty;
    public int dirtyIndex = DIRTY_INDEX++;
    public static int DIRTY_INDEX = 0;

    public Vec3f pos;
    public Vec3f normal;

    public boolean selected;

    public Vertex() {
    }

    public Vertex(Vec3f pos) {
        this.pos = pos;
    }

    public Vertex(Vec3f pos, Vec3f normal) {
        this.pos = pos;
        this.normal = normal;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "pos=" + pos +
                ", normal=" + normal +
                '}';
    }

    public Vertex copy() {//TODO get rid, make immutable
        Vertex result = new Vertex();
        DIRTY_INDEX--;
        result.dirty = dirty;
        result.dirtyIndex = dirtyIndex;
        result.pos = pos;
        result.normal = normal;
        return result;
    }
}

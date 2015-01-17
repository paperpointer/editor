package yk.editor;

import java.util.List;
import java.util.Set;

import static yk.jcommon.utils.Util.set;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/7/14
 * Time: 3:26 PM
 */
public class MenuAutoRemove extends MenuEntry {

    public MenuAutoRemove(String name) {
        super(name);
    }

    @Override
    public void tick(final DDDEditor editor) {
        if (!pressed) return;

        List<Polygon> selected = MenuAutoAdd.getSelectedTriangles(editor);
        if (selected.isEmpty()) return;


        Set<Line> a = set();

        float polyCountWeight = 0.1f;
        float normalDifWeight = 1f;

        Line smallest = null;

        for (Polygon t : selected) {
            for (int indexIndex = 0; indexIndex < t.vertices.size(); indexIndex++) {
                Line line = new Line(t.vertices.get(indexIndex), t.vertices.get(indexIndex == t.vertices.size() - 1 ? 0 : (indexIndex + 1)));
                if (!a.contains(line)) {
                    float size = line.a.pos.dist(line.b.pos);
                    line.weight = size * (1 + inPolysCount(editor, line) * polyCountWeight + line.a.normal.dist(line.b.normal) * normalDifWeight);
                    a.add(line);
                    if (smallest == null || line.weight < smallest.weight) smallest = line;
                }
            }
        }

        editor.replace(smallest.a, smallest.b);
        //TODO measure changes
        editor.fixNormals(true);
    }

    @Override
    public void release(DDDEditor editor) {
        editor.makeHistory();
    }

    public static int inPolysCount(DDDEditor editor, Line line) {
        int result = 0;
        for (Polygon polygon : editor.polygons) if (polygon.vertices.contains(line.a) || polygon.vertices.contains(line.b)) result++;
        return result;
    }

    public static class Line {
        Vertex a, b;
        float weight;

        public Line(Vertex a, Vertex b) {
            if (b.dirtyIndex > a.dirtyIndex) {
                this.a = a;
                this.b = b;
            } else {
                this.a = b;
                this.b = a;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Line line = (Line) o;

            if (!a.equals(line.a)) return false;
            if (!b.equals(line.b)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = a.hashCode();
            result = 31 * result + b.hashCode();
            return result;
        }
    }
}

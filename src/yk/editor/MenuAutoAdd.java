package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static yk.jcommon.utils.Util.list;
import static yk.jcommon.utils.Util.set;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/7/14
 * Time: 3:26 PM
 */
public class MenuAutoAdd extends MenuEntry {

    public MenuAutoAdd(String name) {
        super(name);
    }

    @Override
    public void tick(final DDDEditor editor) {
        if (!pressed) return;
        List<Polygon> selected = getSelectedTriangles(editor);
        if (selected.isEmpty()) return;
        splitLine(editor, getSelectedLines(editor, selected).get(0));
        editor.fixNormals(true);
    }

    @Override
    public void release(DDDEditor editor) {
        editor.makeHistory();
    }

    public static List<Polygon> getSelectedTriangles(final DDDEditor editor) {
        List<Polygon> selected = list();
        for (Polygon polygon : editor.polygons) if (polygon.isSelected()) selected.add(polygon);
        return selected;
    }

    public static List<List<Vertex>> getSelectedLines(final DDDEditor editor, List<Polygon> selected) {
        List<List<Vertex>> lines = list();
        for (Polygon t : selected) {
            for (int indexIndex = 0; indexIndex < t.vertices.size(); indexIndex++) {
                    lines.add(list(t.vertices.get(indexIndex), t.vertices.get(indexIndex == t.vertices.size() - 1 ? 0 : (indexIndex + 1))));
            }
        }
        Collections.sort(lines, new Comparator<List<Vertex>>() {
            @Override
            public int compare(List<Vertex> o1, List<Vertex> o2) {
                return Float.compare(lineSize(o2), lineSize(o1));
            }
        });
        return lines;
    }

    public static void main(String[] args) {

        Vertex va = new Vertex(new Vec3f(0, 0, 100), new Vec3f(0, 1, -1).normalized());
        Vertex vb = new Vertex(new Vec3f(1, 0, 100), new Vec3f(0, 1, 1).normalized());
//
        System.out.println(calcPos(va, vb));
//        Vertex va = new Vertex(new Vec3f(2, 33, 4), new Vec3f(0.4f, 0.1f, 0.9f).normalized());
//        Vertex vb = new Vertex(new Vec3f(2, 37, 4), new Vec3f(0.4f, 0.09f, 0.9f).normalized());
//
//        System.out.println(calcPos(va, vb));
    }


    private static Vertex calcPos(Vertex a, Vertex b) {
        //System.out.println("a: " + a + " b: " + b);
        Vertex result = new Vertex();
        result.normal = a.normal.add(b.normal).normalized();
        result.pos = a.pos.add(b.pos).div(2);

        Vec3f cross = b.normal.crossProduct(a.normal);

        if (cross.length() < 0.0001) return result;
        cross = cross.normalized();
        Vec3f axisX = b.pos.sub(a.pos).normalized();
        Vec3f axisY = axisX.crossProduct(cross);
        if (axisY.length() < 0.001) return result;
        axisY = axisY.normalized();

        //System.out.println("axisX = " + axisX);
        //System.out.println("axisY = " + axisY);

        float X1 = a.normal.scalarProduct(axisX);
        float Y1 = a.normal.scalarProduct(axisY);

        float X2 = b.normal.scalarProduct(axisX);
        float Y2 = b.normal.scalarProduct(axisY);

        //System.out.println("X1 = " + X1 + " " + "Y1 = " + Y1);
        //System.out.println("X2 = " + X2 + " " + "Y2 = " + Y2);

        float A1 = Y1;
        float B1 = -X1;
        float C1 = 0;

        float A2 = Y2;
        float B2 = -X2;
        float C2 = Y2 * a.pos.dist(b.pos);

        //System.out.println("A1 = " + A1 + " " + "B1 = " + B1 + " " + "C1 = " + C1);
        //System.out.println("A2 = " + A2 + " " + "B2 = " + B2 + " " + "C2 = " + C2);

        float RX = (C1 * B2 - B1 * C2) / (A1 * B2 - B1 * A2);
        float RY = (A1 * C2 - C1 * A2) / (A1 * B2 - B1 * A2);
        //System.out.println("RX: " + RX + " RY: " + RY);

        Vec3f R = axisX.mul(RX).add(axisY.mul(RY)).add(a.pos);
        //System.out.println("R: " + R);

        result.pos = a.pos.add(b.pos).div(2).sub(R).normalized().mul((R.dist(a.pos) + R.dist(b.pos)) / 2).add(R);
        //System.out.println(result.pos);


        return result;


    }

    private void splitLine(DDDEditor editor, List<Vertex> indices) {

//        Vertex newVertex = new Vertex(getCommonCenter(indices), getCommonNormal(indices));
        Vertex newVertex = calcPos(indices.get(0), indices.get(1));

        newVertex.selected = true;
        editor.vertices.add(newVertex);
        Set<Vertex> ii = set();
        for (Vertex i : indices) ii.add(i);
        List<Polygon> toRemove = list();
        List<Polygon> toAdd = list();
        for (Polygon t : editor.polygons) if (contains2(t, indices)) {
            toRemove.add(t);
            if (t.vertices.size() == 3) {
                if (!ii.contains(t.vertices.get(0))) {
                    toAdd.add(new Polygon(t.vertices.get(0), t.vertices.get(1), newVertex));
                    toAdd.add(new Polygon(t.vertices.get(0), newVertex, t.vertices.get(2)));
                } else if (!ii.contains(t.vertices.get(1))) {
                    toAdd.add(new Polygon(t.vertices.get(1), t.vertices.get(2), newVertex));
                    toAdd.add(new Polygon(t.vertices.get(1), newVertex, t.vertices.get(0)));
                } else {
                    toAdd.add(new Polygon(t.vertices.get(2), t.vertices.get(0), newVertex));
                    toAdd.add(new Polygon(t.vertices.get(2), newVertex, t.vertices.get(1)));
                }
            } else if (t.vertices.size() == 4) {
                if (ii.contains(t.vertices.get(0)) && ii.contains(t.vertices.get(1))) {
                    toAdd.add(new Polygon(newVertex, t.vertices.get(3), t.vertices.get(0)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(2), t.vertices.get(3)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(1), t.vertices.get(2)));
                } else if (ii.contains(t.vertices.get(1)) && ii.contains(t.vertices.get(2))) {
                    toAdd.add(new Polygon(newVertex, t.vertices.get(0), t.vertices.get(1)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(3), t.vertices.get(0)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(2), t.vertices.get(3)));
                } else if (ii.contains(t.vertices.get(2)) && ii.contains(t.vertices.get(3))) {
                    toAdd.add(new Polygon(newVertex, t.vertices.get(1), t.vertices.get(2)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(0), t.vertices.get(1)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(3), t.vertices.get(0)));
                } else {
                    toAdd.add(new Polygon(newVertex, t.vertices.get(0), t.vertices.get(1)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(1), t.vertices.get(2)));
                    toAdd.add(new Polygon(newVertex, t.vertices.get(2), t.vertices.get(3)));
                }

            } else {
                System.out.println("Can't split edge for polygon " + t);
            }
        }
        editor.polygons.removeAll(toRemove);
        editor.polygons.addAll(toAdd);
    }

    private static boolean contains2(Polygon t, List<Vertex> indices) {
        int found = 0;
        for (Vertex i : indices) {
            for (Vertex i2 : t.vertices) {
                if (i2 == i) found++;
            }
        }
        return found == indices.size();
    }

    public static float lineSize(List<Vertex> line) {
        return line.get(0).pos.dist(line.get(1).pos);
    }

    public static Vec3f getCommonNormal(List<Vertex> indices) {
        Vec3f commonNormal = Vec3f.ZERO;
        for (Vertex i : indices) commonNormal = commonNormal.add(i.normal);
        commonNormal = commonNormal.normalized();
        return commonNormal;
    }

    public static Vec3f getCommonCenter(List<Vertex> indices) {
        Vec3f commonPos = Vec3f.ZERO;
        for (Vertex i : indices) commonPos = commonPos.add(i.pos);
        commonPos = commonPos.div(indices.size());
        return commonPos;
    }

}

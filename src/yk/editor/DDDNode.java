package yk.editor;

import org.lwjgl.util.Color;
import yk.jcommon.fastgeom.IntCube;
import yk.jcommon.tree.CubeNode;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/18/13
 * Time: 9:11 PM
 */
public class DDDNode extends CubeNode {
    public static int[][][] EDGES = new int[][][]{
            {{0, 0, 0}, {1, 0, 0}, {0, 1, 0}, {1, 1, 0}},//near
            {{0, 0, 1}, {1, 0, 1}, {0, 1, 1}, {1, 1, 1}},//far
            {{0, 0, 0}, {0, 1, 0}, {0, 0, 1}, {0, 1, 1}},//left
            {{1, 0, 0}, {1, 1, 0}, {1, 0, 1}, {1, 1, 1}},//right
            {{0, 1, 0}, {1, 1, 0}, {0, 1, 1}, {1, 1, 1}},//bottom
            {{0, 0, 0}, {1, 0, 0}, {0, 0, 1}, {1, 0, 1}},//top
    };
    public static int[] COUNTERS = new int[]{1, 0, 3, 2, 5, 4};
    public float alpha;
    public Color color;
    public boolean solid;

    public void balance() {

    }

    public boolean markSolid() {
        if (children != null) {
            solid = true;
            for (CubeNode child : children) {
                if (!((DDDNode) child).markSolid()) {
                    solid = false;
                }
            }
        }
        return solid;
    }

    public void initNeighbours() {
        if (children != null) {
            for (int i = 0; i < EDGES.length; i++) {
                for (int i1 = 0; i1 < EDGES[i].length; i1++) {
                    if (neigh[i] == null) c(EDGES[i][i1]).neigh[i] = null;
                    else {
                        if (neigh[i].children == null) {
                            c(EDGES[i][i1]).neigh[i] = null;
                        } else {
                            c(EDGES[i][i1]).neigh[i] = neigh[i].c(EDGES[COUNTERS[i]][i1]);
                        }
                    }
                    //c(EDGES[i][i1]).neigh[i] = neigh[i] == null ? null : neigh[i].c(EDGES[COUNTERS[i]][i1]);
                    c(EDGES[i][i1]).neigh[COUNTERS[i]] = c(EDGES[COUNTERS[i]][i1]);
                }
            }
            for (CubeNode child : children) {
                ((DDDNode) child).initNeighbours();
            }
        }
    }

    public Color balanceWithColor() {
        if (children == null) {
            for (CubeNode node : neigh) if (node == null || ((DDDNode)node).color == null) return color;
            return null;
        } else {
            Color common = ((DDDNode) children[0]).balanceWithColor();
            for (int i = 1; i < children.length; i++) {
                if (((DDDNode) children[i]).balanceWithColor() != common) common = null;
            }
            if (common != null) {
                System.out.println("removed" + System.currentTimeMillis());
                removeChildren();
                this.color = common;
            }
            return common;
        }
    }

    public void paint(IntCube cube, Color color) {
        if (this.cube.isCrossed(cube)) {
            if (this.cube.isInside(cube) || this.cube.size == 1) {
                removeChildren();
                this.color = color;
            } else if (children != null) {
                for (CubeNode child : children) ((DDDNode) child).paint(cube, color);
            } else {
                createChildren();
                for (CubeNode child : children) {
                    ((DDDNode) child).color = this.color;
                    ((DDDNode) child).paint(cube, color);
                }
            }
        }
    }

    public void clear(IntCube cube) {
        if (this.cube.isCrossed(cube)) {
            if (this.cube.isInside(cube)) {
                removeChildren();
            } else {
                createChildren();
                for (CubeNode child : children) ((DDDNode) child).clear(cube);
            }
        }
    }

    @Override
    protected void removeChildren() {
        if (children != null) for (CubeNode child : children) ((DDDNode) child).color = null;
        super.removeChildren();
    }
}

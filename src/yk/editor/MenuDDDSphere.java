package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/20/13
 * Time: 11:34 PM
 */
public class MenuDDDSphere extends MenuEntry {

    private org.lwjgl.util.Color c = new org.lwjgl.util.Color(255, 255, 255);

    public MenuDDDSphere(String key, String name) {
        super(name);
    }

    @Override
    public void tick(DDDEditor editor) {
        if (pressed) {
            int size = 6;

            System.out.println("painting at " + editor.arrowSceneSpacePos);
            float mul = 1f;
            for (int x = -size; x <= size; x++) {
                for (int y = -size; y <= size; y++) {
                    for (int z = -size; z <= size; z++) {
                        Vec3f pos = new Vec3f(x, y, z);
                        if (pos.length() < size) {
                            //treeRoot.paint(new IntCube().init(x, y, z, x, y, z), c);
//                            editor.treeRoot.paint(new IntCube().init(new Vec3f(x+editor.arrowSceneSpacePos.x/mul, y+editor.arrowSceneSpacePos.y/mul, z-editor.arrowSceneSpacePos.z/mul), 0), c);
                        }
                    }
                }
                //treeRoot.balanceWithColor();
            }

//            editor.treeRoot.balanceWithColor();
//            editor.treeRoot.initNeighbours();


        }
    }
}

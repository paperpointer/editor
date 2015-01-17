package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/12/13
 * Time: 10:40 PM
 */
public class MenuSwitchBlock extends MenuEntry {


    public MenuSwitchBlock(String key, String name) {
        super(name);
    }

    @Override
    public void click(DDDEditor editor) {
        float size = 8;

        Vec3f pos = editor.arrowSceneSpacePos;
        pos = new Vec3f(quantize(pos.x, size), quantize(pos.y, size), quantize(pos.z, size));

//        for (Iterator<Vec3f> it = editor.points.iterator(); it.hasNext(); ) {
//            if (it.next().dist(pos) < size) {
//                it.remove();
//                return;
//            }
//        }

//        editor.points.add(new Vec3f(pos));
    }


    public static float quantize(float f, float by) {
        int q = (int) (f / by);
        float result = q * by;
        if (f < 0) result -= by;
        return result + by / 2;
    }
}

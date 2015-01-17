package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/6/14
 * Time: 8:24 PM
 */
public class MenuMagnification extends MenuEntry {
    public float step;
    public float limit;

    public MenuMagnification(String name, float step, float limit) {
        super(name);
        this.step = step;
        this.limit = limit;
    }

    @Override
    public void click(DDDEditor editor) {
        Vec3f oldArrowPos = editor.arrowSceneSpacePos;
        editor.magnifier = step > 0 ? Math.min(limit, editor.magnifier + step) : Math.max(limit, editor.magnifier + step);;
        Vec3f newArrowPos = editor.calcArrowSceneSpacePos();
        editor.cam.lookAt = editor.cam.lookAt.add(oldArrowPos.sub(newArrowPos));
    }
}

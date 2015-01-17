package yk.editor;

import yk.jcommon.fastgeom.Vec3f;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 7/4/13
 * Time: 8:15 PM
 */
public class MenuSlowPointer extends MenuEntry {
    private Vec3f startPos;

    public MenuSlowPointer(String key, String name) {
        super(name);
    }

    @Override
    public void press(DDDEditor editor) {
        super.press(editor);
        startPos = editor.arrowSceneSpacePos;
    }

    @Override
    public void release(DDDEditor editor) {
        super.release(editor);
        startPos = null;
    }

    @Override
    public void tick(DDDEditor editor) {
        if (startPos != null) {
            editor.arrowSceneSpacePos = editor.arrowSceneSpacePos.sub(startPos).mul(0.1f).add(startPos);
        }
    }
}

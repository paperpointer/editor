package yk.editor;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/12/13
 * Time: 10:40 PM
 */
public class MenuAddPoint extends MenuEntry {

    public MenuAddPoint(String key, String name) {
        super(name);
    }

    @Override
    public void tick(DDDEditor editor) {
//        if (pressed) editor.points.add(new Vec3f(editor.arrowSceneSpacePos));
    }
}

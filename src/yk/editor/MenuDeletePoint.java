package yk.editor;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/12/13
 * Time: 10:40 PM
 */
public class MenuDeletePoint extends MenuEntry {

    public MenuDeletePoint(String key, String name) {
        super(name);
    }

    @Override
    public void tick(DDDEditor editor) {
        if (pressed) {
//            for (Iterator<Vec3f> it = editor.points.iterator(); it.hasNext(); ) {
//                if (it.next().dist(editor.arrowSceneSpacePos) < 5) it.remove();
//            }
        }
    }
}

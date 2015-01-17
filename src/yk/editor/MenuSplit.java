package yk.editor;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 7/10/13
 * Time: 11:12 PM
 */
public class MenuSplit extends MenuEntry {
    public MenuSplit(String key, String name) {
        super(name);
    }

    @Override
    public void click(DDDEditor editor) {
        if (editor.selectedEdge1 != null) {
            editor.splitEdge(editor.selectedEdge1, editor.selectedEdge2, editor.selectedEdgeDiv);
        }
    }
}

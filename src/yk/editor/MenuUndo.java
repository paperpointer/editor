package yk.editor;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/20/14
 * Time: 4:42 PM
 */
public class MenuUndo extends MenuEntry {
    public boolean redo;

    public MenuUndo(String name, boolean redo) {
        super(name);
        this.redo = redo;
    }

    @Override
    public void click(DDDEditor editor) {
        if (redo) {
            editor.redo();
        } else {
            editor.undo();
        }
    }
}

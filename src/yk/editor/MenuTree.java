package yk.editor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/12/13
 * Time: 10:40 PM
 */
public class MenuTree extends MenuEntry {
    public List<MenuEntry> menu = new ArrayList<MenuEntry>();

    public MenuTree(String key, String name) {
        super(name);
    }

    public void click(DDDEditor editor) {
//        editor.currentMenu = this;
    }
}

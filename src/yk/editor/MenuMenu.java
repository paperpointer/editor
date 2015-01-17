package yk.editor;

import java.util.Map;

import static yk.jcommon.utils.Util.map;


/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/7/14
 * Time: 1:06 PM
 */
public class MenuMenu extends MenuEntry {
    public String myName;
    public MenuMenu parentMenu;

    public boolean entered = false;
    public Map<String, ? extends MenuEntry> children = map();

    public MenuMenu(String name, Map<String, ? extends MenuEntry> cc) {
        super(name);
        myName = name;
        this.children = cc;
        for (MenuEntry child : cc.values()) {
            if (child instanceof MenuMenu) {
                ((MenuMenu)child).parentMenu = this;
            }
        }
    }

    public void check(Map<String, ? extends MenuEntry> available) {
        for (Map.Entry<String, ? extends MenuEntry> child : children.entrySet()) {
            if (available != null && !available.containsKey(child.getKey())) throw new Error("menu " + this + " contains child key " + child.getKey() + " that is not available");
            if (child.getValue() instanceof MenuMenu) ((MenuMenu)child.getValue()).check(children);
        }
    }

    @Override
    public void click(DDDEditor editor) {
        if (entered) {
            parentMenu.exited(editor, this);
            name = myName;
        } else {
            for (Map.Entry<String, ? extends MenuEntry> entry : children.entrySet()) editor.currentMenus.put(entry.getKey(), entry.getValue());
            name = "back";
        }
        entered = !entered;
    }

    public void exited(DDDEditor editor, MenuMenu from) {
        for (String childKey : from.children.keySet()) {
            if (children.get(childKey) == null) {
                editor.currentMenus.remove(childKey);
            } else {
                editor.currentMenus.put(childKey, children.get(childKey));
            }
        }
    }
}

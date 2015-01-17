package yk.editor;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/12/13
 * Time: 10:21 PM
 */
public abstract class MenuEntry {

//    public String key;
    public String name;
    public boolean pressed;

    public MenuEntry(String name) {
//        this.key = key;
        this.name = name;
    }

    public void click(DDDEditor editor){}

    public void press(DDDEditor editor){}

    public void release(DDDEditor editor){}

    public void tick(DDDEditor editor) {}

    @Override
    public String toString() {
        return "MenuEntry{" +
//                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", pressed=" + pressed +
                '}';
    }
}

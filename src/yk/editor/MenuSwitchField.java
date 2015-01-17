package yk.editor;


import yk.jcommon.utils.Reflector;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 7/10/13
 * Time: 8:50 PM
 */
public class MenuSwitchField extends MenuEntry {
    public String fieldName;
    public boolean b;
    public Object valueA;
    public Object valueB;
    public String rawName;

    public MenuSwitchField(String rawName, String fieldName, Object initial, Object firstSwitch) {
        super(rawName + initial);
        this.rawName = rawName;
        this.fieldName = fieldName;
        this.valueA = initial;
        this.valueB = firstSwitch;
    }

    @Override
    public void click(DDDEditor editor) {
        Object newValue = b ? valueA : valueB;
        name = rawName + newValue;
        Reflector.set(editor, fieldName, newValue);
        b = !b;
    }
}

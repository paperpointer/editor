package yk.editor;


import yk.jcommon.utils.Reflector;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 7/10/13
 * Time: 8:50 PM
 */
public class MenuSetField extends MenuEntry {
    public String fieldName;
    public Object pressValue;
    private Object oldValue;

    public MenuSetField(String key, String name, String fieldName, Object pressValue) {
        super(name);
        this.fieldName = fieldName;
        this.pressValue = pressValue;
    }

    @Override
    public void press(DDDEditor editor) {
        oldValue = Reflector.get(editor, fieldName);
        Reflector.set(editor, fieldName, pressValue);
    }

    @Override
    public void release(DDDEditor editor) {
        Reflector.set(editor, fieldName, oldValue);
    }
}

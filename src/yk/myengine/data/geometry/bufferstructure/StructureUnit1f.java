package yk.myengine.data.geometry.bufferstructure;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by: Yuri Kravchik Date: 5 11 2007 Time: 14:09:55
 */
public class StructureUnit1f extends StructureUnit {
    protected StructureUnit1f(int type) {
        super(type, 1 * FLOAT_SIZE);
    }

    public StructureUnit1f(String name) {
        super(name, 1 * FLOAT_SIZE);
    }

    public void get(int index, Vector2f v2f) {
        index = getAddress(index);
        v2f.x = buffer.getFloat(index);
    }

    public void set(int index, float x) {
        index = getAddress(index);
        buffer.putFloat(index, x);
    }
}

package yk.myengine.data.geometry.bufferstructure;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by: Yuri Kravchik Date: 5 11 2007 Time: 14:09:55
 */
public class StructureUnit2f extends StructureUnit {
    protected StructureUnit2f(int type) {
        super(type, 2 * FLOAT_SIZE);
    }

    public StructureUnit2f(String name) {
        super(name, 2 * FLOAT_SIZE);
    }

    public void get(int index, Vector2f v2f) {
        index = getAddress(index);
        v2f.x = buffer.getFloat(index);
        index += FLOAT_SIZE;
        v2f.y = buffer.getFloat(index);
    }

    public void set(float... ff) {
        for (int i = 0, ffLength = ff.length; i < ffLength; i+=2) {
            buffer.putFloat(getAddress(i/2), ff[i]);
            buffer.putFloat(getAddress(i/2)+FLOAT_SIZE, ff[i+1]);
        }
    }

    public void set(int index, float x, float y) {
        index = getAddress(index);
        buffer.putFloat(index, x);
        index += FLOAT_SIZE;
        buffer.putFloat(index, y);
    }

    public void set(int index, Vector2f v2f) {
        index = getAddress(index);
        buffer.putFloat(index, v2f.x);
        index += FLOAT_SIZE;
        buffer.putFloat(index, v2f.y);
    }
}

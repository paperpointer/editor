package yk.myengine.data.geometry.bufferstructure;

import org.lwjgl.util.vector.Vector4f;

/**
 * Created by: Yuri Kravchik
 * Date: 5 11 2007
 * Time: 12:17:13
 */
public class StructureUnit4f extends StructureUnit {

    protected StructureUnit4f(int type) {
        super(type, 4 * FLOAT_SIZE);
    }

    public StructureUnit4f(String name) {
        super(name, 4 * FLOAT_SIZE);
    }

    public void set(float... ff) {
        for (int i = 0, ffLength = ff.length; i < ffLength; i+=4) {
            buffer.putFloat(getAddress(i/4), ff[i]);
            buffer.putFloat(getAddress(i/4)+FLOAT_SIZE, ff[i+1]);
            buffer.putFloat(getAddress(i/4)+FLOAT_SIZE+FLOAT_SIZE, ff[i+2]);
            buffer.putFloat(getAddress(i/4)+FLOAT_SIZE+FLOAT_SIZE+FLOAT_SIZE, ff[i+3]);
        }
    }

    public void set(int index, float a, float x, float y, float z) {
        index = getAddress(index);
        buffer.putFloat(index, a);
        index += FLOAT_SIZE;
        buffer.putFloat(index, x);
        index += FLOAT_SIZE;
        buffer.putFloat(index, y);
        index += FLOAT_SIZE;
        buffer.putFloat(index, z);
    }

    //public void set(int index, Vector3f v3f){
    //    index = getAddress(index);
    //    buffer.putFloat(index, v3f.x);
    //    index += FLOAT_SIZE;
    //    buffer.putFloat(index, v3f.y);
    //    index += FLOAT_SIZE;
    //    buffer.putFloat(index, v3f.z);
    //}

    public void get(int index, Vector4f v4f){
        index = getAddress(index);
        v4f.w = buffer.getFloat(index);
        index += FLOAT_SIZE;
        v4f.x = buffer.getFloat(index);
        index += FLOAT_SIZE;
        v4f.y = buffer.getFloat(index);
        index += FLOAT_SIZE;
        v4f.z = buffer.getFloat(index);
    }
}

package yk.myengine.data.geometry.bufferstructure;

/**
 * Created by: Yuri Kravchik
 * Date: 5 11 2007
 * Time: 12:17:13
 */
public class StructureUnit3f extends StructureUnit {

    protected StructureUnit3f(int type) {
        super(type, 3 * FLOAT_SIZE);
    }

    public StructureUnit3f(String name) {
        super(name, 3 * FLOAT_SIZE);
    }

    public void set(float... ff) {
        for (int i = 0, ffLength = ff.length; i < ffLength; i+=3) {
            buffer.putFloat(getAddress(i/3), ff[i]);
            buffer.putFloat(getAddress(i/3)+FLOAT_SIZE, ff[i+1]);
            buffer.putFloat(getAddress(i/3)+FLOAT_SIZE+FLOAT_SIZE, ff[i+2]);
        }
    }

    public void set(int index, float x, float y, float z) {
        index = getAddress(index);
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

    //public void get(int index, Vector3f v3f){
    //    index = getAddress(index);
    //    v3f.x = buffer.getFloat(index);
    //    index += FLOAT_SIZE;
    //    v3f.y = buffer.getFloat(index);
    //    index += FLOAT_SIZE;
    //    v3f.z = buffer.getFloat(index);
    //}
}

package yk.myengine.data.geometry.bufferstructure;

/**
 * Created by: Yuri Kravchik
 * Date: 5 11 2007
 * Time: 12:56:31
 */
abstract public class StructureUnit4b extends StructureUnit {
    protected StructureUnit4b(int type) {
        super(type, 4);
    }

    public void set(int index, byte r, byte g, byte b, byte a) {
        index = getAddress(index);
        buffer.put(index, r);
        index++;
        buffer.put(index, g);
        index++;
        buffer.put(index, b);
        index++;
        buffer.put(index, a);
    }

}

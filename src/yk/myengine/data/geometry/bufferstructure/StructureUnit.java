package yk.myengine.data.geometry.bufferstructure;

import java.nio.ByteBuffer;

/**
 * Created by: Yuri Kravchik Date: 2/11/2007 Time: 11:13:59
 */
public class StructureUnit {
    public static final int FLOAT_SIZE = 4;
    /**
     * Used to mark unused region in data sequence. Could be created only
     * through StructureAttribType because its constructor accepts custom size.
     */
    public static final int BUFFERTYPE_UNUSED = 0;

    //gl11
    public static final int BUFFERTYPE_VERTEX3f = 1;
    public static final int BUFFERTYPE_NORMAL3f = 2;
    public static final int BUFFERTYPE_COLOR4b = 3;
    public static final int BUFFERTYPE_TEXCOORD2f = 4;
    public static final int BUFFERTYPE_EDGEFLAG = 5;

    //gl14
    public static final int BUFFERTYPE_SECONDARYCOLOR = 6;
    public static final int BUFFERTYPE_FOGCOORD = 7;

    //gl20
    public static final int BUFFERTYPE_VERTEXATTRIB = 10;

    protected int type;
    /**
     * Size in bytes.
     */
    protected int size;
    protected String dataName = "";
    int stride;
    ByteBuffer buffer;
    int offset;

    protected StructureUnit(final int type, final int size) {
        this.type = type;
        this.size = size;
    }

    public StructureUnit(final String dataName, final int size) {
        this(BUFFERTYPE_VERTEXATTRIB, size);
        this.dataName = dataName;
    }

    protected int getAddress(final int index) {
        return stride * index + offset;
    }

    /**
     * Checks for TYPE equality. Checks for equality next fields:
     * <ul>
     * <li>type
     * <li>size
     * <li>dataName
     * </ul>
     * offset is not checked, because it depends on bundle (bundle did not
     * checket too). Whole bundle will check offset.
     *
     * @param obj another structure unit
     * @return true - type is identical (type cast is possible)
     */
    public boolean equals(final StructureUnit obj) {
        return type == obj.type && size == obj.size && dataName.equals(obj.dataName);
    }

    public String getName() {
        return dataName;
    }

    public int getSize() {
        return size;
    }

    public int getType() {
        return type;
    }
}

package yk.myengine.data.geometry.bufferstructure;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright Yuri Kravchik 2007 Date: 03.11.2007 Time: 23:01:53
 */
public class StructureBundleTight {
    public List<StructureUnit> units;
    public int stride;
    public String shaderProgram;
    ByteBuffer buffer;

    public StructureBundleTight(final List<StructureUnit> units) {
        this.units = units;
        recalcStride();
    }

    public StructureBundleTight(final StructureUnit... units) {
        this.units = new LinkedList<StructureUnit>(Arrays.asList(units));
        recalcStride();
    }

    private void recalcStride() {
        stride = 0;
        for (final StructureUnit t : units) {
            t.offset = stride;
            stride += t.getSize();
        }
    }

    public void copyLocal(final int num, final int source, final int destination, final int stride) {
        for (int i = 0; i < num; i++) {
            buffer.position();
            for (int j = 0; j < this.stride; j++) {
                buffer.put(this.stride * (destination + stride * i) + j, buffer.get(this.stride
                        * (source + stride * i) + j));
            }
        }
    }

    public boolean equals(final StructureBundleTight obj) {
        return stride == obj.stride && shaderProgram.equals(obj.shaderProgram)
                && units.equals(obj.units);
    }

    /**
     * Realized only for equally typed, tightly composed (interleaved)
     * bundles!!!
     *
     * @param sourceBundle        source bundle
     * @param num                 number of elements to copy
     * @param sourcePosition      source position
     * @param sourceStride        number of elements between one and other. Must be 1
     *                            or greater (can be with sign -)
     * @param destinationPosition destination position
     * @param destinationStride   number of elements between one and other. Must
     *                            be 1 or greater (can be with sign -)
     */
    public void fillFrom(final StructureBundleTight sourceBundle, final int num,
                         final int sourcePosition, final int sourceStride, final int destinationPosition,
                         final int destinationStride) {
        for (int i = 0; i < num; i++) {
            buffer.position(stride * (destinationPosition + destinationStride * i));
            if (sourceBundle.stride * (sourcePosition + sourceStride * i) > sourceBundle.buffer
                    .limit()) {
                System.out.println("ERROR! TOO BIG.");
                return;
            }
            sourceBundle.buffer.position(sourceBundle.stride * (sourcePosition + sourceStride * i));
            for (int j = 0; j < stride; j++) {
                buffer.put(sourceBundle.buffer.get());
            }
        }
        sourceBundle.buffer.rewind();
        buffer.rewind();
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Finds structure unit with the same type in this bundle.
     *
     * @param unit similar unit
     * @return similar unit in this bundle of the same type as input
     *         StructureUnit
     */
    public StructureUnit getSimilar(final StructureUnit unit) {
        for (final StructureUnit local : units) {
            if (local.equals(unit)) {
                return local;
            }
        }
        return null;
    }

    /**
     * Sets current buffer. Without buffer null pointer exception will rise when
     * try to read or write something.
     *
     * @param buffer byte buffer to operate on
     */
    public void setBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
        for (final StructureUnit structureUnit : units) {
            structureUnit.stride = stride;
            structureUnit.buffer = buffer;
        }
    }
}

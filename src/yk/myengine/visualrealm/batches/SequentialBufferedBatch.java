package yk.myengine.visualrealm.batches;

import yk.myengine.data.geometry.bufferstructure.StructureBundleTight;
import yk.myengine.data.geometry.bufferstructure.StructureUnit;
import yk.myengine.optiseq.Sequence;
import yk.myengine.optiseq.states.DataBufferState;
import yk.myengine.optiseq.states.DrawRangeState;
import yk.myengine.optiseq.states.SpatialStacked;
import yk.myengine.optiseq.states.VertexStructureState;
import yk.myengine.optiseq.states.shaders.ShaderHandler;
import yk.myengine.optiseq.states.shaders.VertexAttrib;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

//import surface.GLTexture;

/**
 * Created by: Yuri Kravchik Date: 8 11 2007 Time: 13:54:24
 */
public class SequentialBufferedBatch extends RenderingBatchBase {
    /*  TODO there is another handler layer possible
     *  HeightFieldHandler: heightfield -> geometrybatch (local structure generation - geometry)
     *  BatchHandler: geometrybatch -> sequence (tied structure generation - lights, shadows, materials)
     */
    private boolean turnedOn = true;
    private final SpatialStacked transformation = new SpatialStacked();
    private Sequence sequence;
    private DataBufferState dataBufferState;
    private ShaderHandler shader;
    private int primitiveType = GL11.GL_TRIANGLES;
    //material
    protected Sequence textureSequence = new Sequence();//todo make something else

    //shader
    //..
    protected ShortBuffer indexBuffer;
    protected StructureBundleTight structure;
    public ByteBuffer buffer;
    public boolean dirtyBuffer = false;

    //primitive topology
    //..

    public SequentialBufferedBatch(final StructureBundleTight structure, final int itemsNumber) {
        this.structure = structure;
        buffer = BufferUtils.createByteBuffer(itemsNumber * structure.stride);
        structure.setBuffer(buffer);
    }

    @Override
    public void draw() {
        if (turnedOn) {
            if (dirtyBuffer) {
                dataBufferState.bufferData(buffer);
                dirtyBuffer = false;
            }
            textureSequence.enable();
            sequence.enable();
            sequence.disable();
            textureSequence.disable();
        }
    }

    public Sequence getSequence() {
        return sequence;
    }

    public StructureBundleTight getStructure() {
        return structure;
    }

    public Sequence getTextureSequence() {
        return textureSequence;
    }

    //public void addTexture(GLTexture texture) {
    //    textureSequence.getStates().add(texture);
    //}

    public SpatialStacked getTransformation() {
        return transformation;
    }

    @Override
    public void init() {
        sequence = new Sequence();
        sequence.getStates().add(transformation);
        //sequence.getStates().affect(textureSequence);
        sequence.getStates().add(lightState);
        if (shader != null) {
            sequence.getStates().add(shader);
        }
        dataBufferState = new DataBufferState(buffer, 0, true);
        sequence.getStates().add(dataBufferState);
        sequence.getStates().add(new VertexStructureState(structure, shader));
        sequence.getStates().add(
                new DrawRangeState(indexBuffer,
                        primitiveType,
                        0,
                        indexBuffer.limit()));
    }

    @Override
    public void makeRelease() {
        sequence.release();
//        sequence = null;
//        buffer = null;
//        indexBuffer = null;
//        structure.setBuffer(null);
    }

    public void setIndices(int... ss) {
        ShortBuffer indices = BufferUtils.createShortBuffer(ss.length);
        for (int s : ss) indices.put((short)s);
        indices.rewind();
        setIndices(indices);
    }

    public void setIndices(final ShortBuffer buffer) {
        indexBuffer = buffer;
    }

    public void setPrimitiveType(final int primitiveType) {
        this.primitiveType = primitiveType;
    }

    public void setShader(final ShaderHandler shader) {
        this.shader = shader;

        //for (State state : textureSequence.getStates()) {
        //    GLTexture t = (GLTexture) state;
        //    shader.addVariables(UniformVariable.createVariable(t.getName(), 0));
        //}

        for (StructureUnit unit : structure.units) {
            shader.addVertexAttrib(new VertexAttrib(unit.getName(), GL11.GL_FLOAT, unit.getSize()/4));//TODO fix - FLOAT, fix 4
        }
        shader.initVariables();
    }

    public void turnOnOff(final boolean onOff) {
        turnedOn = onOff;
    }

}

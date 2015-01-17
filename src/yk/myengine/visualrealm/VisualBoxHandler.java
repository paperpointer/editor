/**
 * File VisualBoxHandler.java
 * @author Yuri Kravchik
 * Created 16.01.2008
 */
package yk.myengine.visualrealm;

import yk.myengine.data.geometry.bufferstructure.StructureBundleTight;
import yk.myengine.data.geometry.bufferstructure.StructureUnitColor4b;
import yk.myengine.data.geometry.bufferstructure.StructureUnitVertex3f;
import yk.myengine.visualrealm.batches.SequentialBufferedBatch;

import java.nio.ShortBuffer;

/**
 * VisualBoxHandler
 *
 * @author Yuri Kravchik Created 16.01.2008
 */
public class VisualBoxHandler {//TODO must be in geometry source in some way
    //FIXME not ended class
    private SequentialBufferedBatch renderingBatch;
    protected StructureUnitVertex3f vertices = new StructureUnitVertex3f();
    protected StructureUnitColor4b colors = new StructureUnitColor4b();

    public VisualBoxHandler(float size) {
        renderingBatch = new SequentialBufferedBatch(new StructureBundleTight(vertices, colors), 8);

        renderingBatch.setIndices(createIndices());
        fillCubeCoords(vertices, size);
    }

    private ShortBuffer createIndices() {
        return null;
    }

    private void fillCubeCoords(StructureUnitVertex3f coords, float size) {

    }
}

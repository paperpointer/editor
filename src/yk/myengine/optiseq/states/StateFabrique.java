package yk.myengine.optiseq.states;

import yk.myengine.data.geometry.bufferstructure.StructureBundleTight;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Yuri Kravchik 2007
 * Date: 03.11.2007
 * Time: 23:14:09
 */
public class StateFabrique {
    private Map<StructureBundleTight, VertexStructureState> formatToStructure = new HashMap<StructureBundleTight, VertexStructureState>();

    public VertexStructureState getVertexStructureState(StructureBundleTight structureBundle) {
        VertexStructureState state = formatToStructure.get(structureBundle);
        if (state == null) {
            //todo: shaders!
            state = new VertexStructureState(structureBundle, null);
            formatToStructure.put(structureBundle, state);
        }
        return state;
    }
}

package yk.myengine.visualrealm;

import yk.myengine.optiseq.Sequence;
import yk.myengine.optiseq.states.CameraState;
import yk.myengine.optiseq.states.CameraTransformationState;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by: Yuri Kravchik
 * Date: 11 11 2007
 * Time: 13:04:55
 */
public class CameraHandler {
    private CameraTransformationState camTrans;
    private Sequence sequence;

    public CameraHandler() {
        sequence = new Sequence();
        camTrans = new CameraTransformationState();
        sequence.getStates().add(new CameraState());
        sequence.getStates().add(camTrans);
    }

    public Sequence getSequence() {
        return sequence;
    }

    public CameraTransformationState getCamTrans() {
        return camTrans;
    }

    public void getPosition(Vector3f result) {
        camTrans.getPosition(result);
    }
}

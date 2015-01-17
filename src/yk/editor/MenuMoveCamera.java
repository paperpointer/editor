package yk.editor;

import yk.jcommon.fastgeom.Quaternionf;
import yk.jcommon.fastgeom.Vec3f;
import yk.jcommon.utils.Cam;


/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/12/13
 * Time: 11:00 PM
 */
public class MenuMoveCamera extends MenuEntry {
    private Vec3f startBrushPos;
    private Quaternionf startBrushRot;
    private Cam startCam;

    public MenuMoveCamera(String name) {
        super(name);
    }


    @Override
    public void click(DDDEditor editor) {
    }

    @Override
    public void press(DDDEditor editor) {
        super.press(editor);
        startBrushPos = editor.ddd.arrowCamSpacePos.mul(editor.magnifier);
        startBrushRot = editor.ddd.arrowCamSpaceRot;
        //lastBrushPos = startBrushPos;
        startCam = new Cam(editor.cam);
    }

    @Override
    public void tick(DDDEditor editor) {
        super.tick(editor);
        if (pressed) {
            //need more understanding
            Quaternionf rotDif = startBrushRot.mul(startCam.lookRot).conjug().mul(editor.ddd.arrowCamSpaceRot.mul(startCam.lookRot)).normalized();
            editor.cam.lookRot = startCam.lookRot.mul(rotDif.conjug());
            Vec3f startArrowScenePos = startCam.lookAt.add(startCam.lookRot.rotateFast(startBrushPos));
            editor.cam.lookAt = startCam.lookAt.sub(startCam.lookRot.rotateFast(editor.ddd.arrowCamSpacePos.mul(editor.magnifier).sub(startBrushPos)));
            editor.cam.lookAt = rotDif.conjug().rotateFast(editor.cam.lookAt.sub(startArrowScenePos)).add(startArrowScenePos);

            //good moving
            //editor.cam.lookAt = startCam.lookAt.sub(startCam.lookRot.conjug().rotateFast(editor.ddd.arrowCamSpacePos.sub(startBrushPos)));
        }
    }

}

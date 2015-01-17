package yk.paperpointer;

import com.github.sarxos.webcam.Webcam;
import yk.editor.JustExit;
import yk.jcommon.fastgeom.Quaternionf;
import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.raster.rgb.INyARRgbRaster;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.core.types.matrix.NyARDoubleMatrix44;
import jp.nyatla.nyartoolkit.detector.NyARSingleDetectMarker;
import jp.nyatla.nyartoolkit.utils.j2se.NyARBufferedImageRaster;
import yk.jcommon.fastgeom.Vec3f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/8/13
 * Time: 2:40 PM
 */
public class DDDBrush {
    public float MUL = .08f;//TODO rename or even calculate from normal config params
    public Vec3f
     arrowCamSpacePos = new Vec3f(0, 0, 100);
    public Quaternionf arrowCamSpaceRot = Quaternionf.ZERO;
    public BufferedImage bi;

//    private FrameGrabber grabber;
    private NyARSingleDetectMarker _nya;

    private int w;
    private int h;
    private NyARTransMatResult matrix = new NyARTransMatResult();

    private Webcam webcam = null;

    public DDDBrush() {
        this(640, 480);
    }

    public DDDBrush(int w, int h) {
        this.w = w;
        this.h = h;
        init();
    }

    public static Quaternionf NyMtoQ(NyARDoubleMatrix44 matrix) {
        return new Quaternionf(
                (float) matrix.m00, (float) matrix.m01, (float) matrix.m02,
                (float) matrix.m10, (float) matrix.m11, (float) matrix.m12,
                (float) matrix.m20, (float) matrix.m21, (float) matrix.m22
        );
    }

    public void init() {
        try {
            NyARParam param = new NyARParam();
            param.loadARParam(new FileInputStream("Data/camera_para.dat"));
            param.changeScreenSize(w, h);

            NyARCode code = new NyARCode(16, 16);
            code.loadARPatt(new FileInputStream("Data/patt.hiro"));

//            WebcamPicker picker = new WebcamPicker();//TODO add UI for camera selection
            webcam = Webcam.getDefault();
            if (webcam == null) {
                JOptionPane.showMessageDialog(null, "Can't get default camera. Exiting :_(");
                throw new JustExit("Can't get default camera. Exiting :_(");
            }
            webcam.close();
            webcam.setViewSize(new Dimension(w, h));
            webcam.open();

            _nya = NyARSingleDetectMarker.createInstance(param, code, 80.);
            _nya.setContinueMode(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private int posTries = 5;
    private int rotTries = 5;
    public float curPosDif;
    public float curRotDif;
    public boolean out;

    public void tick() {

        try {
            if (!webcam.isOpen()) return;
            if (!webcam.isImageNew()) return;
            bi = webcam.getImage();//TODO read in other thread
            INyARRgbRaster raster = new NyARBufferedImageRaster(bi);


            if (_nya.detectMarkerLite(raster, 130)) {
                _nya.getTransmat(matrix);
            }

        } catch (Exception e) {
            e.printStackTrace();
//            throw new Error(e);
        }

        NyARDoubleMatrix44 res = new NyARDoubleMatrix44();
        NyARDoubleMatrix44 trans = new NyARDoubleMatrix44();
        trans.identity();
        res.setValue(matrix);
        Quaternionf newRot = NyMtoQ(res);
        newRot = new Quaternionf(newRot.a, newRot.i, newRot.j, -newRot.k).normalized();

        if (newRot.rotateFast(new Vec3f(0, 0, -1)).z < 0.5) { //straight look is 0, 0, 1
            out = true;
            rotTries = 5;
            posTries = 5;
            return;
        }

        curRotDif = arrowCamSpaceRot.dist(newRot);
        if (curRotDif > 0.2 && rotTries > 0) {
            rotTries--;
        } else {
            rotTries = 5;
            arrowCamSpaceRot = newRot;
        }

        Vec3f newPos = new Vec3f((float) matrix.m03 * -MUL, (float) matrix.m13 * -MUL, (float) matrix.m23 * MUL).add(0, 0, -100f);
        curPosDif = newPos.dist(arrowCamSpacePos);
        if (curPosDif > 10 && posTries > 0) {
            posTries--;
        } else {
            posTries = 5;
            arrowCamSpacePos = newPos;
        }
    }

}

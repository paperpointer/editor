package yk.editor;

import yk.myengine.optiseq.states.shaders.ShaderHandler;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import yk.paperpointer.DDDBrush;
import yk.test.Primitives;
import yk.jcommon.fastgeom.Quaternionf;
import yk.jcommon.fastgeom.Vec3f;
import yk.jcommon.utils.DDDUtils;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/8/13
 * Time: 2:46 PM
 */
public class DDDBrushTest {
    public int w = 800;
    public int h = 600;
    public DDDBrush ddd = new DDDBrush();
    public ShaderHandler shaders = new ShaderHandler();
    public DrawMe drawMe = new DrawMe();

    public static void main(String[] args) throws IOException, LWJGLException {
        new DDDBrushTest().start();
    }

    public void start() throws IOException, LWJGLException {
        initGL(w, h);
        shaders.createProgram("shader/simpleVS.txt", "shader/simpleFS.txt");
        drawMe.init(w, h);
        while (true) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClear(GL11.GL_DEPTH_BUFFER_BIT);
            render();

            Display.update();
            Display.sync(100);

            if (Display.isCloseRequested()) {
                Display.destroy();
                System.exit(0);
            }
        }
    }

    public void render() {
        ddd.tick();
        glBindTexture(GL_TEXTURE_2D, 0);
        glDepthMask(true);
        shaders.enable();

        setCam(true);
        drawArrow(ddd.arrowCamSpaceRot, ddd.arrowCamSpacePos);
        drawMe.drawImg(ddd.bi);

        shaders.disable();
    }

    private void initGL(int width, int height) throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create(new PixelFormat(0, 8, 1));
        Display.setVSyncEnabled(true);
        glEnable(GL_DEPTH_TEST);
        //TODO what is this?
        //NyARGLUtil.toCameraFrustumRH(this._ar_param,1,10,10000,this._camera_projection);
    }

    private void setCam(boolean left) {
        glMatrixMode(GL11.GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45.0f, (float) w / h, 1.0f, 1000.0f);
        glMatrixMode(GL11.GL_MODELVIEW);
        glLoadIdentity();
        //multMatrix(camQ.conjug());
        //glTranslatef(-camPos.x + (left ? -2 : 2), -camPos.y, -camPos.z);
    }

    private void drawArrow(Quaternionf arrowSceneSpaceRot, Vec3f arrowSceneSpacePos) {
        glTranslatef(arrowSceneSpacePos.x, arrowSceneSpacePos.y, arrowSceneSpacePos.z);
        DDDUtils.multMatrix(arrowSceneSpaceRot);
        glScalef(4, 4, 4);
        //drawPointer();
        Primitives.drawCube(true);
    }
}

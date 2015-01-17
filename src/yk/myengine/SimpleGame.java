/**
 * File SimpleGame.java
 * @author Yuri Kravchik
 * Created 17 01 2008
 */
package yk.myengine;

import yk.myengine.optiseq.states.shaders.ShaderHandler;
import yk.myengine.optiseq.states.shaders.UniformVariable;
import yk.myengine.visualrealm.CameraHandler;
import yk.myengine.visualrealm.VisualRealmManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.text.DecimalFormat;

/**
 * SimpleGame
 *
 * @author Yuri Kravchik Created 17 01 2008
 */
abstract public class SimpleGame {
    private static int keyboardInputPause = 0;
    public static final DecimalFormat formatter = new DecimalFormat();

    public static final ShaderHandler shader = new ShaderHandler();

    static {
        formatter.setMinimumFractionDigits(4);
    }

    /**
     * Do any game-specific cleanup
     */
    private static void cleanup() {
        // Close the window
        System.out.println("");
        final FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, fb);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print("\t\t\t" + formatter.format(fb.get(j * 4 + i)));
            }
            System.out.println("");
        }
        Display.destroy();
    }

    /**
     * Exit the game
     */
    private boolean closeRequested;

    private boolean windowActive;

    protected VisualRealmManager visualRealmManager;

    /**
     * Game title
     */
    public String GAME_TITLE = "My Game";

    private void handleKeyInput() {
        final CameraHandler cameraHandler = visualRealmManager.getCameraHandler();
        // Example input handler: we'll check for the ESC key and finish
        // the
        // game instantly when it's pressed
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            closeRequested = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            cameraHandler.getCamTrans().rotAV();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            cameraHandler.getCamTrans().rotV();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            cameraHandler.getCamTrans().rotAH();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            cameraHandler.getCamTrans().rotH();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            cameraHandler.getCamTrans().strifeLeft();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            cameraHandler.getCamTrans().strifeRight();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            cameraHandler.getCamTrans().stepForward();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            cameraHandler.getCamTrans().stepBackward();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            cameraHandler.getCamTrans().up();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            cameraHandler.getCamTrans().down();
        }

        if (keyboardInputPause > 0) {
            keyboardInputPause--;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            if (keyboardInputPause == 0) {
                keyboardInputPause = 10;
                visualRealmManager.setWireframe(!visualRealmManager.isWireframe());
            }
        }

    }

    /**
     * Initialise the game
     *
     * @throws Exception if init fails
     */
    private void initDisplaySystem(final boolean fullscreen) throws Exception {
        // Create a fullscreen window with 1:1 orthographic 2D
        // projection
        // (default)
        Display.setTitle(GAME_TITLE);
        Display.setFullscreen(fullscreen);

        // Enable vsync if we can (due to how OpenGL works, it cannot be
        // guarenteed to always work)
        Display.setVSyncEnabled(true);

        // Create default display of 640x480
        Display.create();
        //System.loadLibrary("glew32");

        visualRealmManager = new VisualRealmManager();
        initShader();


    }

    public void initShader() {
        //TODO: make varargs
        shader.addVariables(UniformVariable.createVariable("BrickColor", 1, 0.3f, 0.2f),
                UniformVariable.createVariable("MortarColor", 0.85f, 0.86f, 0.84f),
                UniformVariable.createVariable("LightPosition", 0, 0, 4),
                UniformVariable.createVariable("BrickSize", 10f, 4.5f),
                UniformVariable.createVariable("BrickPct", 0.90f, 0.85f));

        shader.createProgram(new String[]{"simpleVS.txt"},
                new String[]{"shader/simpleFS.txt"});
    }

    /**
     * Render the current frame
     */
    private void render() {
        visualRealmManager.render();
    }

    /**
     * Runs the game (the "main loop")
     */
    private void run() {

        while (!closeRequested) {
            logic();
            renderSystem();
            if (!windowActive) {
                // The window is not in the foreground, so we
                // can allow other
                // stuff to run and
                // infrequently update
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                }
            }
        }
    }

    abstract protected void initGeometry();

    protected boolean isKeyDown(final int keyNumber) {
        return Keyboard.isKeyDown(keyNumber);
    }

    abstract protected void logic();

    public void beginGame(final boolean fullScreen) {
        // Sys.alert("Welcome", "Welcome!");
        try {
            initDisplaySystem(fullScreen);
            initGeometry();
            run();
        } catch (final Throwable e) {
            e.printStackTrace(System.err);
            Sys.alert(GAME_TITLE, "An error occured and the game will exit.");
        } finally {
            cleanup();
        }
    }

    public boolean isWindowActive() {
        return windowActive;
    }

    public void renderSystem() {
        if (closeRequested) {
            return;
        }
        // Always call Window.update(), all the time - it does some
        // behind the
        // scenes work, and also displays the rendered output
        Display.update();
        handleKeyInput();
        windowActive = Display.isActive();
        // Check for close requests
        if (Display.isCloseRequested()) {
            closeRequested = true;
            return;
        }

        // The window is in the foreground, so we should play the game
        if (windowActive) {
            render();
            // Display.sync(FRAMERATE);
            return;
        }

        // Only bother rendering if the window is visible or dirty
        // if (Display.isVisible() || Display.isDirty()) {
        // render();
        // }
    }

    public void setMoveSpeed(final float speed) {
        visualRealmManager.getCameraHandler().getCamTrans().setMoveSpeed(speed);
    }

}

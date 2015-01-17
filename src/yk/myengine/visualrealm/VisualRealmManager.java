package yk.myengine.visualrealm;

import yk.myengine.optiseq.states.PolygonWireframeMode;
import yk.myengine.visualrealm.batches.RenderingBatchBase;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.TrueTypeFont;
import yk.jcommon.utils.XYit;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by: Yuri Kravchik Date: 11 ���� 2007 Time: 13:35:40
 */
public class VisualRealmManager {
    private static Resources resources;

    public static ByteBuffer convertToGL(final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        ByteBuffer data;
        final int[] buf = new int[width * height * 3];
        data = BufferUtils.createByteBuffer(width * height * 4);
        image.getData().getPixels(0, 0, image.getWidth(), image.getHeight(), buf);
        for (int i = 0; i < buf.length; i += 3) {
            data.put((byte) buf[i]);
            data.put((byte) buf[i + 1]);
            data.put((byte) buf[i + 2]);
            data.put((byte) 255);
        }
        data.rewind();
        return data;
    }

    public static ByteBuffer convertToGLUnoptimized(final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        ByteBuffer data;
        data = BufferUtils.createByteBuffer(width * height * 4);
        for (XYit xy : XYit.im(image)) {
            Color color = new Color(image.getRGB(xy.x, xy.y), true);
            data.put((byte) color.getRed());
            data.put((byte) color.getGreen());
            data.put((byte) color.getBlue());
            data.put((byte) color.getAlpha());
        }
        data.rewind();
        return data;
    }

    private final List<String> textOutput = new ArrayList<String>();

    private long timerRender = 0;

    private long timer1 = 0;
    private final CameraHandler cameraHandler;
    private boolean wireframe = false;
    private final List<RenderingBatchBase> batches = new LinkedList<RenderingBatchBase>();
//    private final SimplestTextureState texState;
    private final PolygonWireframeMode wireframeMode = new PolygonWireframeMode();

    TrueTypeFont font;

    //private final LinkedList<HeightFieldHandler> availableHandlers = new LinkedList<HeightFieldHandler>();
    //private final LinkedList<HeightFieldHandler> freshHandlers = new LinkedList<HeightFieldHandler>();

//    private final LWJGLFont lwjglFont = new LWJGLFont();
    //todo make my own picture-holder (for ByteBuffered picture)
//    private final SimplestTextureState fontTexture;

    private String temp1;

    private String temp2;

    private int counter;

    public long timerLogic;

    private final static Vector3f defaultTextScale = new Vector3f(1, 1, 1);

    public VisualRealmManager() throws IOException {
        cameraHandler = new CameraHandler();
        resources = new Resources();
//        final BufferedImage im = ImageIO.read(new File("res/avatar_lighting.PNG"));
//        texState = new SimplestTextureState(VisualRealmManager.convertToGL(im),
//                im.getWidth(),
//                im.getHeight());
//        fontTexture = new SimplestTextureState(TGALoader.createRGBA("res/defaultfont.tga"),//TODO implement texts
//                512,
//                512);
//        todo call destroy! and call destroy for everything else
//        lwjglFont.buildDisplayList();


// load a default java font
        font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 18), false);

    }

    private void drawSomeText() {

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMatrixMode(GL11.GL_PROJECTION);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        final long fullRoutine = System.nanoTime() - timer1;
        if (counter == 0) {
            counter = 10;
            temp1 = " logic: " + timerLogic / 1000000 + "  rendering: " + timerRender / 1000000
                    + " full: " + fullRoutine / 1000000;
            temp2 = " FPS: " + (1000000000 / (fullRoutine + 1));
            //            temp1 = 100 * timerRender / fullRoutine;
        } else {
            counter--;
        }
        timer1 = System.nanoTime();
//        fontTexture.enable();
        font.drawString(20, 20, temp2);
        final Vector3f v3f = new Vector3f();
        cameraHandler.getPosition(v3f);
        font.drawString(20, 40, v3f.toString());
        font.drawString(20, 60, "Percent in render: " + temp1);
//        font.drawString(20, 80, "Size of buffer: " + availableHandlers.size(), 0);

        for (int i = 0; i < textOutput.size(); i++) {
            font.drawString(20, 100 + i * 20, textOutput.get(i) == null ? "null" : textOutput.get(i));
        }


        GL11.glDisable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
    }

    private void refreshBuffer() {
        //availableHandlers.addAll(freshHandlers);
        //freshHandlers.clear();
    }

    public void addBatch(final RenderingBatchBase batch) {
        batches.add(batch);
        //shader and
        //material
        //        stitch.getRenderingBatch().getTextureSequence().getStates().affect(texState);

        //light
        batch.setLight(resources.getSimplestLightState());
        batch.init();
    }

    //public void freshHandler(final HeightFieldHandler handler) {
    //    freshHandlers.add(handler);
    //}

    //todo introduce HORIZONTAL/VERTICAL variables?

    //todo    ��������� �������� stitch � Surface, ���� ������ ��� ���/����
    //todo    � ����� �������� � stitch �����������, ������������� stitch ��� ���������� �� surface
    //todo    (����� horizontal � vertical �� �����������)

    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    //public HeightFieldHandler getHandler() {
    //    if (availableHandlers.size() == 0) {
    //        availableHandlers.add(new HeightFieldHandler(10, 10));
    //    }
    //    return availableHandlers.removeFirst();
    //}

    public boolean isWireframe() {
        return wireframe;
    }

    public void render() {
        timerRender = System.nanoTime();
        refreshBuffer();

        if (wireframe) {
            wireframeMode.enable();
        }
        cameraHandler.getSequence().enable();

        final ListIterator<RenderingBatchBase> li = batches.listIterator();
        RenderingBatchBase renderingBatch;
        while (li.hasNext()) {
            renderingBatch = li.next();
            if (renderingBatch.release) {
                renderingBatch.makeRelease();
                li.remove();
            } else {
                renderingBatch.draw();
            }
        }

        cameraHandler.getSequence().disable();
        if (wireframe) {
            wireframeMode.disable();
        }

        timerRender = System.nanoTime() - timerRender;
        drawSomeText();
    }

    public void setOutputMessage(final int lineNumber, final String s) {
        while (textOutput.size() <= lineNumber) {
            textOutput.add(null);
        }
        textOutput.set(lineNumber, s);
    }

    public void setWireframe(final boolean wireframe) {
        this.wireframe = wireframe;
    }

    //public void stitchVertical(final HeightField top, final int topFrom, final int topSize,
    //                           final HeightField bottom, final int bottomFrom, final int bottomSize) {
    //    final StitchHandler stitch = new StitchHandler(topSize,
    //            bottomSize,
    //            new StructureBundleTight(new StructureUnitVertex3f(),
    //                    new StructureUnitNormal3f(),
    //                    new StructureUnitTexCoor()));
    //    stitch.init(top.visualHandler,
    //            bottom.visualHandler,
    //            StitchHandler.SOUTH,
    //            StitchHandler.NORTH,
    //            topFrom,
    //            bottomFrom);
    //    batches.add(stitch.getRenderingBatch());
    //
    //    //shader and
    //    //material
    //    stitch.getRenderingBatch().getTextureSequence().getStates().add(texState);
    //
    //    //light
    //    stitch.getRenderingBatch().setLight(resources.getSimplestLightState());
    //    stitch.getRenderingBatch().init();
    //}
}

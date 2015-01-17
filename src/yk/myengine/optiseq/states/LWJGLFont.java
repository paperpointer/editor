package yk.myengine.optiseq.states;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.ByteBuffer;

//TODO fix license issue


/**
 * Created by: Yuri Kravchik
 * Date: 22 ���� 2007
 * Time: 15:31:51
 */

/**
 * todo: remove! <code>Font2D</code> maintains display lists for each ASCII
 * character defined by an image. <code>Font2D</code> assumes that the texture
 * is 256x256 and that the characters are 16 pixels high by 16 pixels wide. The
 * order of the characters is also important: <br>
 * <p/>
 * <img src ="fonttable.gif"> <br>
 * <p/>
 * After the font is loaded, it can be used with a call to <code>print</code>.
 * The <code>Font2D</code> class is also printed in Ortho mode and billboarded,
 * as well as depth buffering turned off. This means that the font will be
 * placed at a two dimensional coordinate that corresponds to screen
 * coordinates.
 * <p/>
 * The users is assumed to set a TextureState to the Text Geometry calling this
 * class.
 * <p/>
 * //* @see com.jme.scene.Text
 * //* @see com.jme.scene.state.TextureState
 *
 * @author Mark Powell
 * @version $Id: LWJGLFont.java,v 1.19 2007/09/14 20:53:53 nca Exp $
 */
public class LWJGLFont {

    /**
     * Sets the style of the font to normal.
     */
    public static final int NORMAL = 0;

    /**
     * Sets the style of the font to italics.
     */
    public static final int ITALICS = 1;

    //display list offset.
    private int base;

    //buffer that holds the text.
    private ByteBuffer scratch;

    //Color to render the font.
    private final Vector4f fontColor;

    /**
     * Constructor instantiates a new <code>LWJGLFont</code> object. The initial
     * color is set to white.
     */
    public LWJGLFont() {
        fontColor = new Vector4f(1, 1, 1, 1);
        scratch = BufferUtils.createByteBuffer(1);
        buildDisplayList();
    }

    /**
     * <code>buildDisplayList</code> sets up the 256 display lists that are used
     * to render each font character. Each list quad is 16x16, as defined by the
     * font image size.
     */
    public void buildDisplayList() {
        final int displacement = 10;
        final int symbolWidth = 16;
        final int symbolHeight = 16;
        final int symbolNumX = 16;
        final int symbolNumY = 16;
        final int symbolNumber = 256;
        float cx;
        float cy;
        final float texSymSizeX = (float) 1 / symbolNumX;
        final float texSymSizeY = (float) 1 / symbolNumY;
        base = GL11.glGenLists(symbolNumber);
        final float addDisY = texSymSizeY / 100;

        for (int loop = 0; loop < symbolNumber; loop++) {
            cx = (loop % symbolNumX) * texSymSizeX;
            cy = (loop / symbolNumY) * texSymSizeX;

            GL11.glNewList(base + loop, GL11.GL_COMPILE);
            GL11.glBegin(GL11.GL_QUADS);

            GL11.glTexCoord2f(cx, 1 - cy - addDisY);
            GL11.glVertex2i(0, symbolHeight);

            GL11.glTexCoord2f(cx, 1 - cy - texSymSizeY + addDisY);
            GL11.glVertex2i(0, 0);

            GL11.glTexCoord2f(cx + texSymSizeX, 1 - cy - texSymSizeY + addDisY);
            GL11.glVertex2i(symbolWidth, 0);

            GL11.glTexCoord2f(cx + texSymSizeX, 1 - cy - addDisY);
            GL11.glVertex2i(symbolWidth, symbolHeight);

            GL11.glEnd();
            GL11.glTranslatef(displacement, 0, 0);
            GL11.glEndList();
        }
    }

    /**
     * <code>deleteFont</code> deletes the current display list of font objects.
     * The font will be useless until a call to <code>buildDisplayLists</code>
     * is made.
     */
    public void deleteFont() {
        GL11.glDeleteLists(base, 256);
    }

    /**
     * <code>print</code> renders the specified string to a given (x,y)
     * location. The x, y location is in terms of screen coordinates. There are
     * currently two sets of fonts supported: NORMAL and ITALICS.
     *
     * @param x      the x screen location to start the string render.
     * @param y      the y screen location to start the string render.
     * @param scale  scale
     * @param text   the String to render.
     * @param italic the mode of font: NORMAL or ITALICS.
     */
    public void print(final int x, final int y, final Vector3f scale, final String text, int italic) {
//        RendererRecord matRecord = (RendererRecord) DisplaySystem.getDisplaySystem().getCurrentContext().getRendererRecord();
        if (italic > 1) {
            italic = 1;
        } else if (italic < 0) {
            italic = 0;
        }

//        if (!alreadyOrtho){
        //todo move to outer function
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluOrtho2D(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode()
                .getHeight());
//        }
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale.x, scale.y, scale.z);
        //todo: make italic by matrix, instead of additional data!! (matter of principle)
        //also it must be realized in transformation state together with scale factor!
        GL11.glListBase(base - 32 + (128 * italic));

        //Put the string into a "pointer"
        if (text.length() > scratch.capacity()) {
            scratch = BufferUtils.createByteBuffer(text.length());
        } else {
            scratch.clear();
        }

        final int charLen = text.length();
        for (int z = 0; z < charLen; z++) {
            scratch.put((byte) text.charAt(z));
        }
        scratch.flip();
        GL11.glColor4f(fontColor.x, fontColor.y, fontColor.z, fontColor.w);
        //todo move to right place
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //call the list for each letter in the string.
        GL11.glCallLists(scratch);
        GL11.glDisable(GL11.GL_BLEND);
//        set color back to white
//        GL11.glColor4f(fontColor.x, fontColor.y, fontColor.z, fontColor.w);

//        if (!alreadyOrtho) {
        //todo move to outer function
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
//        }
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
    }

    public void setColor(final Vector4f v4f) {
        fontColor.set(v4f);
    }

    /**
     * <code>toString</code> returns the string representation of this font
     * object in the Format: <br>
     * <br>
     * jme.geometry.hud.text.Font2D@1c282a1 <br>
     * Color: {RGBA COLOR} <br>
     *
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        String string = super.toString();
        string += "\nColor: " + fontColor.toString();

        return string;
    }
}
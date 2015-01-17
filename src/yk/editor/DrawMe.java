package yk.editor;

import yk.myengine.optiseq.states.ColorState4f;
import yk.myengine.optiseq.states.shaders.ShaderHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDepthMask;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/11/13
 * Time: 10:32 PM
 */
public class DrawMe {
    public static final float SIZE = .2f;
    ShaderHandler shaders = new ShaderHandler();
    int textureID;
    int w, h;
    public ColorState4f color = new ColorState4f(1, 1, 1, 1);

    public void init(int w, int h) {
        this.w = w;
        this.h = h;

        shaders.createProgram("shader/drawMeVS.txt", "shader/drawMeFS.txt");

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //!!! must be in draw?
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void drawImg(BufferedImage bi) {
        if (bi == null) return;

        shaders.disable();

        glColor3f(1, 1, 1);
        ByteBuffer buffer = BufferUtils.createByteBuffer(bi.getWidth() * bi.getHeight() * 3);
        byte[] bytes = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < bytes.length; i += 3) {
            byte a = bytes[i];
            byte b = bytes[i+2];
            bytes[i] = b;
            bytes[i+2] = a;
        }


        buffer.put(bytes);
        buffer.flip();

        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, bi.getWidth(), bi.getHeight(), 0, GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, w, h, 0, 1, -1);

        glMatrixMode(GL11.GL_MODELVIEW);
        glLoadIdentity();

        glDepthMask(false);
        int posx = 0;
        int posy = 0;

        color.enable();
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex2f(posx, posy);
        glTexCoord2f(0, 0);
        glVertex2f(posx + bi.getWidth()* SIZE, posy);
        glTexCoord2f(0, 1);
        glVertex2f(posx + bi.getWidth()* SIZE, posy + bi.getHeight()* SIZE);

        glTexCoord2f(1, 1);
        glVertex2f(posx, posy + bi.getHeight()* SIZE);
        glEnd();

        glDepthMask(true);

        shaders.disable();
    }

}

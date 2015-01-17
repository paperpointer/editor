package yk.test;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 * Kravchik Yuri
 * Date: 12.07.2012
 * Time: 9:23 PM
 */
public class Stereo {

    public static void main(String[] args) {
        new Stereo().start();
    }

    public void start() {
        initGL(800, 600);
        //init();

        while (true) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClear(GL11.GL_DEPTH_BUFFER_BIT);
            display();

            Display.update();
            Display.sync(100);

            if (Display.isCloseRequested()) {
                Display.destroy();
                System.exit(0);
            }
        }

    }


    private void initGL(int width, int height) {
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create(new PixelFormat(0, 8, 1));

            Display.setVSyncEnabled(true);

            // diagnostic
            IntBuffer ib = BufferUtils.createIntBuffer(100);
            glGetInteger(GL_STENCIL_BITS, ib);
            ib.rewind();
            System.out.println("Number of stencil buffer bits " + ib.get(0));

        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        interlace_stencil(width, height);

    }


    public static void display() {
//	glDrawBuffer(GL_BACK_LEFT);
        // following comand replace glDrawBuffer(GL_BACK_LEFT);
        glStencilFunc(GL_NOTEQUAL, 1, 1); // draws if stencil <> 1

        glDrawBuffer(GL_BACK);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); /* black */
        // the clearing trick des not work becouse clearing does not
        // care about stencil buffer content
        glClear(GL_COLOR_BUFFER_BIT);
        // we must realy draw something
        glColor4f(0.8f, 0.0f, 0.0f, 1.0f); /* red */
        glBegin(GL_QUADS);
        glVertex2f(100, 100);
        glVertex2f(100, 300);
        glVertex2f(300, 300);
        glVertex2f(300, 100);
        glEnd();

        //	glDrawBuffer(GL_BACK_RIGHT);
        // following comand replace glDrawBuffer(GL_BACK_RIGHT);
        glStencilFunc(GL_EQUAL, 1, 1); // draws if stencil <> 0

        // we can not do clearing (erasing previous image)
        //	glClearColor(0.0, 0.0, 1.0, 1.0); /* blue */
        //	glClear(GL_COLOR_BUFFER_BIT);
        // writing something to test the stencil operation
        glColor4f(0, 0, 1, 1); // blue
        glBegin(GL_QUADS);
        glVertex2f(90, 100);
        glVertex2f(90, 300);
        glVertex2f(290, 300);
        glVertex2f(290, 100);
        glEnd();
        // cretaing the stereo interlaced image
        // getting into screen coordinates

        // end of red buffer restoring
        //glutSwapBuffers();
    }

    public static void interlace_stencil(int gliWindowWidth, int gliWindowHeight) {
        int gliY;
        // seting screen-corresponding geometry
        glViewport(0, 0, gliWindowWidth, gliWindowHeight);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f, gliWindowWidth - 1, 0.0f, gliWindowHeight - 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // clearing and configuring stencil drawing
        glDrawBuffer(GL_BACK);
        glEnable(GL_STENCIL_TEST);
        glClearStencil(0);
        glClear(GL_STENCIL_BUFFER_BIT);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE); // colorbuffer is copied to stencil
        glDisable(GL_DEPTH_TEST);
        glStencilFunc(GL_ALWAYS, 1, 1); // to avoid interaction with stencil content

        // drawing stencil pattern
        glColor4f(1, 1, 1, 0);    // alfa is 0 not to interfere with alpha tests
        for (gliY = 0; gliY < gliWindowHeight; gliY += 2) {
            glLineWidth(1);
            glBegin(GL_LINES);
            glVertex2f(0, gliY);
            glVertex2f(gliWindowWidth, gliY);
            glEnd();
        }
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP); // disabling changes in stencil buffer
        glFlush();
    }
}

package yk.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * <p/>
 * Tests AWTGLCanvas functionality
 * <p/>
 *
 * @author $Author$
 *         $Id$
 * @version $Revision$
 */
public class AWTTest extends Frame {

    /**
     * AWT GL canvas
     */
    private AWTGLCanvas canvas0, canvas1;

    private volatile float angle;

    /**
     * C'tor
     */
    public AWTTest() throws LWJGLException {
        setTitle("LWJGL AWT Canvas Test");
        setSize(640, 320);
        setLayout(new GridLayout(1, 2));
        add(canvas0 = new AWTGLCanvas() {
            public void paintGL() {
                try {
                    glViewport(0, 0, getWidth(), getHeight());
                    glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
                    glClear(GL_COLOR_BUFFER_BIT);
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    gluOrtho2D(0.0f, (float) getWidth(), 0.0f, (float) getHeight());
                    glMatrixMode(GL_MODELVIEW);
                    glPushMatrix();
                    glColor3f(1f, 1f, 0f);
                    glTranslatef(getWidth() / 2.0f, getHeight() / 2.0f, 0.0f);
                    glRotatef(angle, 0f, 0f, 1.0f);
                    glRectf(-50.0f, -50.0f, 50.0f, 50.0f);
                    glPopMatrix();
                    swapBuffers();
                    repaint();
                } catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        add(canvas1 = new AWTGLCanvas() {
            public void paintGL() {
                try {
                    angle += 1.0f;
                    glViewport(0, 0, getWidth(), getHeight());
                    glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
                    glClear(GL_COLOR_BUFFER_BIT);
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    gluOrtho2D(0.0f, (float) getWidth(), 0.0f, (float) getHeight());
                    glMatrixMode(GL_MODELVIEW);
                    glPushMatrix();
                    glTranslatef(getWidth() / 2.0f, getHeight() / 2.0f, 0.0f);
                    glRotatef(2 * angle, 0f, 0f, -1.0f);
                    glRectf(-50.0f, -50.0f, 50.0f, 50.0f);
                    glPopMatrix();
                    swapBuffers();
                    repaint();
                } catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        setResizable(true);
        setVisible(true);
    }

    public static void main(String[] args) throws LWJGLException {
        new AWTTest();
    }
}
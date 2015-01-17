package yk.test;

import org.lwjgl.opengl.GL11;
import yk.jcommon.fastgeom.Vec3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Kravchik Yuri
 * Date: 03.07.2012
 * Time: 9:31 PM
 */
public class Primitives {

    public static void drawCube(boolean colored) {
        drawCube(colored, 1, 1);
    }

    public static void drawCube(boolean colored, float alpha, float magnifier){
        glBegin(GL_QUADS);
        if (colored)glColor4f(1, 1, 1, alpha);

        glVertex3f(-magnifier, -magnifier, -magnifier);
        glVertex3f(-magnifier, magnifier, -magnifier);
        glVertex3f(magnifier, magnifier, -magnifier);
        glVertex3f(magnifier, -magnifier, -magnifier);

        if (colored)glColor4f(0, 0, 1, alpha);
        glVertex3f(-magnifier, -magnifier, magnifier);
        glVertex3f(-magnifier, magnifier, magnifier);
        glVertex3f(magnifier, magnifier, magnifier);
        glVertex3f(magnifier, -magnifier, magnifier);

        if (colored)glColor4f(0, 1, 0, alpha);
        glVertex3f(-magnifier, -magnifier, -magnifier);
        glVertex3f(-magnifier, -magnifier, magnifier);
        glVertex3f(-magnifier, magnifier, magnifier);
        glVertex3f(-magnifier, magnifier, -magnifier);

        if (colored)glColor4f(1, 0, 0, alpha);
        glVertex3f(magnifier, -magnifier, -magnifier);
        glVertex3f(magnifier, -magnifier, magnifier);
        glVertex3f(magnifier, magnifier, magnifier);
        glVertex3f(magnifier, magnifier, -magnifier);

        if (colored)glColor4f(1, 0, 1, alpha);
        glVertex3f(-magnifier, -magnifier, -magnifier);
        glVertex3f(-magnifier, -magnifier, magnifier);
        glVertex3f(magnifier, -magnifier, magnifier);
        glVertex3f(magnifier, -magnifier, -magnifier);

        if (colored)glColor4f(1, 1, 0, alpha);
        glVertex3f(-magnifier, magnifier, -magnifier);
        glVertex3f(-magnifier, magnifier, magnifier);
        glVertex3f(magnifier, magnifier, magnifier);
        glVertex3f(magnifier, magnifier, -magnifier);

        glEnd();
    }

    public static void drawMesh(Vert[][] mesh) {
        glBegin(GL11.GL_TRIANGLES);
        for (int x = 0; x < mesh.length-1; x++) for (int y = 0; y < mesh[0].length-1; y++) {
            Vec3f a = mesh[x][y].pos;
            Vec3f b = mesh[x][y+1].pos;
            Vec3f c = mesh[x+1][y].pos;
            Vec3f normal = b.sub(a).crossProduct(c.sub(a)).normalized();
            glNormal3f(normal.x, normal.y, normal.z);
            glTexCoord2f(0, 0);
            v(mesh[x][y]);
            glNormal3f(normal.x, normal.y, normal.z);
            glTexCoord2f(1, 0);
            v(mesh[x + 1][y]);
            glNormal3f(normal.x, normal.y, normal.z);
            glTexCoord2f(0, 1);
            v(mesh[x][y + 1]);
            glNormal3f(normal.x, normal.y, normal.z);
            glTexCoord2f(1, 1);
            v(mesh[x + 1][y + 1]);
            glNormal3f(normal.x, normal.y, normal.z);
            glTexCoord2f(1, 0);
            v(mesh[x + 1][y]);
            glNormal3f(normal.x, normal.y, normal.z);
            glTexCoord2f(0, 1);
            v(mesh[x][y+1]);
        }
        glEnd();

    }

    public static void drawMeshEdge(Vert[][] mesh) {
        glBegin(GL11.GL_LINES);
        for (int x = 0; x < mesh.length-1; x++) for (int y = 0; y < mesh[0].length-1; y++) {
            v(mesh[x][y]);
            v(mesh[x+1][y]);
            v(mesh[x+1][y]);
            v(mesh[x][y+1]);
            v(mesh[x][y+1]);
            v(mesh[x][y]);

            v(mesh[x+1][y+1]);
            v(mesh[x+1][y]);
            v(mesh[x+1][y]);
            v(mesh[x][y+1]);
            v(mesh[x][y+1]);
            v(mesh[x+1][y+1]);
        }
        glEnd();

    }

    private static void v(Vert v) {
        glVertex3f(v.pos.x, v.pos.y, v.pos.z);
    }
}

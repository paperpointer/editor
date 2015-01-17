package yk.test;

import org.lwjgl.opengl.GL11;
import yk.jcommon.fastgeom.Vec3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Kravchik Yuri
 * Date: 17.07.2012
 * Time: 9:55 PM
 */
public class Quad {
    public Vec3f[] all = new Vec3f[4];

    public void enable() {
        Vec3f normal = calcNormal();
        glBegin(GL11.GL_TRIANGLES);

        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(all[0].x, all[0].y, all[0].z);
        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(all[1].x, all[1].y, all[1].z);
        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(all[2].x, all[2].y, all[2].z);

        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(all[0].x, all[0].y, all[0].z);
        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(all[2].x, all[2].y, all[2].z);
        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(all[3].x, all[3].y, all[3].z);

        glEnd();
    }

    private Vec3f calcNormal() {
        Vec3f a = all[1].sub(all[0]).crossProduct(all[2].sub(all[0])).normalized();
        Vec3f b = all[2].sub(all[0]).crossProduct(all[3].sub(all[0])).normalized();
        return a.add(b).div(2);
    }

}

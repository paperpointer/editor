package yk.editor;

import yk.myengine.optiseq.states.shaders.ShaderHandler;
import yk.myengine.optiseq.states.shaders.Uniform1f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import yk.paperpointer.DDDBrush;
import yk.test.Primitives;
import yk.jcommon.fastgeom.Quaternionf;
import yk.jcommon.fastgeom.Vec3f;
import yk.jcommon.loader.WavefrontObj;
import yk.jcommon.utils.Cam;
import yk.jcommon.utils.DDDUtils;
import yk.jcommon.utils.ErrorAlert;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.util.Arrays.asList;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import static yk.jcommon.utils.Util.list;
import static yk.jcommon.utils.Util.map;

/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 3/8/13
 * Time: 2:46 PM
 */
public class DDDEditor {
    public int w = 854;//TODO change size
    public int h = 480;
    //paper pointer
    public DDDBrush ddd = new DDDBrush();
    //    public PaperClient ddd = new PaperClient();
    public Vec3f arrowSceneSpacePos = Vec3f.ZERO;
    public Quaternionf arrowSceneSpaceRot = new Quaternionf(1, 0, 0, 0);
    public float magnifier = 1;

    //draw
    public ShaderHandler shaders = new ShaderHandler();
    public ShaderHandler shaders2 = new ShaderHandler();
    public boolean wireframe;
    public TrueTypeFont font;
    public TrueTypeFont font2;
    public Cam cam = new Cam();
    //UI
    public DrawMe drawMe = new DrawMe();
    //    public MenuTree currentMenu;
    private Map<String, Key> keys = map();

//    public List<Vec3f> smallPoints = list();

    public Vertex selectedEdge1;
    public Vertex selectedEdge2;
    public Vec3f selectedEdgeDiv;

    public List<Vertex> vertices = list();
    public List<Polygon> polygons = list();

    public float dt;

    public JFrame frame = new JFrame();
    public Canvas canvas;

    public boolean showNormals;

    public Map<String, MenuEntry> currentMenus = map();
    public Map<String, ? extends MenuEntry> hiddenMenu = map("ctrl z", new MenuUndo("undo", false), "ctrl shift z", new MenuUndo("redo", true));

    public static void main(String[] args) throws IOException, LWJGLException {
        try {
            int result = JOptionPane.showConfirmDialog(null, "The brighter light conditions leads to better experience.\n\n This is closed test version for demonstration only. Use it on your own risk.\nGo to http://paperpointer.com for more info and new versions.", "Start confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) return;
            DDDEditor editor = new DDDEditor();
            editor.start();
        } catch (JustExit je) {
            System.out.println("Just exiting: " + je.getMessage());
            System.exit(1);
        } catch (Throwable t) {
            t.printStackTrace();
            new ErrorAlert(t);
        }
    }

    long lastTime = System.currentTimeMillis();

    public void start() throws IOException, LWJGLException {
        frame = new JFrame();

//        ddd.connect();
        ddd.init();

        initGL(w, h);
        new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if (!firstStart) {
                long curTime = System.currentTimeMillis();
                dt = ((float) (curTime - lastTime)) / 1000;
                lastTime = curTime;
                tick(dt);
                render();
                Display.update();
                //canvas.repaint();
//                }
            }
        }).start();
    }

    Uniform1f renderDisplacement = new Uniform1f("Displacement", 1);
    Uniform1f renderUseLight = new Uniform1f("UseLight", 1);

    public void firstInit() {
        shaders.addVariables(renderDisplacement, renderUseLight);
        shaders.createProgram("shader/simpleVS.txt", "shader/simpleFS.txt");
        shaders2.createProgram("shader/specular/specularVS.txt", "shader/specular/specularFS.txt");
        drawMe.init(w, h);
        drawMe.color.v4f.set(1, 1, 1, 0.5f);

        font = new org.newdawn.slick.TrueTypeFont(new Font("Times New Roman", Font.PLAIN, 16), false);
        font2 = new org.newdawn.slick.TrueTypeFont(new Font("Times New Roman", Font.BOLD, 16), false);

        for (String letter : letters) {
            keys.put(letter, new Key(Keyboard.getKeyIndex(letter.toUpperCase())));
        }

        MenuMenu mainMenu = new MenuMenu("main menu", map(
                "w", new MenuSoftMove("drag hard", 6, true),
                "e", new MenuSoftMove("drag soft", 6, false),
                "r", new MenuAutoAdd("auto add"),
                "t", new MenuAutoRemove("auto remove"),
                "a", new MenuSoftLens("blow out", 6, false),
                "s", new MenuSoftLens("blow up", -6, false),
//                "a", new MenuMenu("sub menu", map(
//                    "s", new MenuSoftMove("move 2", 3, true))
//
//                    ),
                "z", new MenuMoveCamera("move camera"),
                "x", new MenuMagnification("zoom out", 0.1f, 3),
                "c", new MenuMagnification("zoom in", -0.1f, 0.1f),
                "v", new MenuSwitchField("show normals: ", "showNormals", false, true)
        ));
        mainMenu.check(null);
        mainMenu.click(this);

        //split triangle
        //eat triangle
        //eat edge
        //eat vertex


        glClearColor(0.8f, 0.8f, 0.8f, 0);


        cam.lookAt = new Vec3f(0, 30, 50);
        loadFromFile("Man_bolvanka_new.obj");

    }

    public void loadFromFile(String name) {
        WavefrontObj obj = WavefrontObj.readFromFile(name);
        vertices.clear();
        for (int i = 0; i < obj.vertices.size(); i++) vertices.add(new Vertex(obj.vertices.get(i), obj.normals.get(i)));
        polygons.clear();
        for (List<WavefrontObj.FaceV> face : obj.faces) {
            Polygon p = new Polygon();
            for (int i = 0; i < face.size(); i++) p.vertices.add(vertices.get(face.get(i).v));
            polygons.add(p);
        }
        fixNormals(false);
        historyPolygons.clear();
        historyVertices.clear();
        historyPosition = -1;
        makeHistory();
    }

    public void writeToFile(String name) {
        WavefrontObj obj = new WavefrontObj();
        for (Vertex vertex : vertices) {
            obj.vertices.add(vertex.pos);
            obj.normals.add(vertex.normal);
        }
        for (Polygon polygon : polygons) {
            List<WavefrontObj.FaceV> poly = list();
            obj.faces.add(poly);
            for (Vertex v : polygon.vertices) {
                int i = vertices.indexOf(v);
                poly.add(new WavefrontObj.FaceV(i, i));
            }
        }
        obj.saveToFile(name);
    }

    public void fixNormals(boolean selectedOnly) {
        //TODO fix for quads
        for (Polygon polygon : polygons) if (!selectedOnly || polygon.isSelected()) {
            Vec3f a = polygon.vertices.get(0).pos;
            Vec3f b = polygon.vertices.get(1).pos;
            Vec3f c = polygon.vertices.get(2).pos;
            polygon.normal = b.sub(a).crossProduct(c.sub(a)).normalized();
        }

        for (Vertex vertex : vertices) if (!selectedOnly || vertex.selected) {
            Vec3f sum = Vec3f.ZERO;
            for (Polygon polygon : polygons) if (polygon.vertices.contains(vertex)) sum = sum.add(polygon.normal);
            vertex.normal = sum.normalized();
        }
    }

    private List<String> letters = asList("q", "w", "e", "r", "t", "a", "s", "d", "f", "g", "z", "x", "c", "v", "b");

    public void tick(float dt) {
        tickKeyboard();
        ddd.tick();
        arrowSceneSpacePos = calcArrowSceneSpacePos();
        arrowSceneSpaceRot = ddd.arrowCamSpaceRot.mul(cam.lookRot);

        for (MenuEntry entry : currentMenus.values()) entry.tick(this);

        workMenus(new HashMap<String, MenuEntry>(currentMenus));
        workMenus(hiddenMenu);

        selectedEdge1 = null;
        selectedEdge2 = null;
        selectedEdgeDiv = null;

        for (Polygon polygon : polygons) {
            Vec3f[] ttt = new Vec3f[3];
            ttt[0] = polygon.vertices.get(0).pos;
            ttt[1] = polygon.vertices.get(1).pos;
            ttt[2] = polygon.vertices.get(2).pos;
            Vertex[] vvv = new Vertex[3];
            vvv[0] = polygon.vertices.get(0);
            vvv[1] = polygon.vertices.get(1);
            vvv[2] = polygon.vertices.get(2);
            Vec3f proj = DDDUtils.getStrictTriangleProjection(arrowSceneSpacePos, ttt);
            //if (proj != null) smallPoints.add(proj);

            trySelectEdge(vvv[0], vvv[1], DDDUtils.getStrictLineProjection(arrowSceneSpacePos, ttt[0], ttt[1]));
            trySelectEdge(vvv[1], vvv[2], DDDUtils.getStrictLineProjection(arrowSceneSpacePos, ttt[1], ttt[2]));
            trySelectEdge(vvv[2], vvv[0], DDDUtils.getStrictLineProjection(arrowSceneSpacePos, ttt[2], ttt[0]));

            //Vec3f lineProj = DDDUtils.getStrictLineProjection(arrowSceneSpacePos, ttt[0], ttt[1]);
            //if (lineProj != null) smallPoints.add(lineProj);
            //lineProj = DDDUtils.getStrictLineProjection(arrowSceneSpacePos, ttt[1], ttt[2]);
            //if (lineProj != null) smallPoints.add(lineProj);
            //lineProj = DDDUtils.getStrictLineProjection(arrowSceneSpacePos, ttt[2], ttt[0]);
            //if (lineProj != null) smallPoints.add(lineProj);
        }
    }

    public static boolean canReact(String keystroke, Key keyState) {
        return keystroke.contains("ctrl") == keyState.ctrl
                && keystroke.contains("shift") == keyState.shift
                && keystroke.contains("alt") == keyState.alt;
    }

    public void workMenus(Map<String, ? extends MenuEntry> copy) {
        for (Map.Entry<String, ? extends MenuEntry> entry : copy.entrySet()) {
            String keyStroke = entry.getKey();
            Key keyState = keys.get(keyStroke.substring(keyStroke.length() - 1));
            if (keyState.v == 1) {
                if (canReact(keyStroke, keyState)) {
                    entry.getValue().press(this);
                    entry.getValue().pressed = true;
                }
            }
            if (keyState.v == 3) {
                if (canReact(keyStroke, keyState)) {
                    entry.getValue().click(this);
                }
                entry.getValue().release(this);
                entry.getValue().pressed = false;
            }
        }
    }

    public Vec3f calcArrowSceneSpacePos() {
        return cam.lookAt.add(cam.lookRot.rotateFast(ddd.arrowCamSpacePos.mul(magnifier)));
    }

    private void tickKeyboard() {
        for (Key key : keys.values()) {
            key.ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            key.shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
            key.alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
            if (Keyboard.isKeyDown(key.k)) {
                if (key.v == 2) ;
                else if (key.v == 1) key.v = 2;
                else key.v = 1;
            } else {
                if (key.v == 2 || key.v == 1) {
                    key.v = 3;
                }
                else if (key.v == 3) key.v = 0;
            }
        }
    }

    public void trySelectEdge(Vertex a, Vertex b, Vec3f pos) {
        if (pos == null) return;
        if (arrowSceneSpacePos.sub(pos).length() > 2) return;
        selectedEdge1 = a;
        selectedEdge2 = b;
        selectedEdgeDiv = pos;
    }

    public void render() {
        glEnable(GL_DEPTH_TEST);

        glClear(GL_COLOR_BUFFER_BIT);
        glClear(GL11.GL_DEPTH_BUFFER_BIT);

        glBindTexture(GL_TEXTURE_2D, 0);
        glDepthMask(true);

//        if (wireframe) {
//            glPolygonMode(GL_FRONT, GL_LINE);
//            glPolygonMode(GL_BACK, GL_LINE);
//        }

        renderDisplacement.value = 1;
        renderUseLight.value = 1;
        shaders.enable();
//        shaders2.enable();
        renderTriangles(0.5f, false);

        glPolygonMode(GL_FRONT, GL_LINE);
        glPolygonMode(GL_BACK, GL_LINE);
        renderDisplacement.value = 0.999f;
        renderUseLight.value = 0;
//        shaders.enable();
        shaders2.enable();
        renderTriangles(0.2f, true);

        glPolygonMode(GL_FRONT, GL_POINT);
        glPolygonMode(GL_BACK, GL_POINT);
        renderDisplacement.value = 0.99f;
//        shaders.enable();
        shaders2.enable();
        renderDots(1f, true);

        renderDisplacement.value = 1;
//        shaders.enable();
        shaders2.enable();
//        if (wireframe) {
//            glPolygonMode(GL_FRONT, GL_FILL);
//            glPolygonMode(GL_BACK, GL_FILL);
//        }

        glPolygonMode(GL_FRONT, GL_FILL);
        glPolygonMode(GL_BACK, GL_FILL);

        setCam();

        drawArrow(arrowSceneSpaceRot, arrowSceneSpacePos);

        //noinspection ConstantConditions
        if (ddd instanceof DDDBrush) drawMe.drawImg(((DDDBrush) ddd).bi);

        shaders2.disable();
        drawText();
    }

    private void renderTriangles(float color, boolean selectedOnly) {
        setCam();
        glBegin(GL_TRIANGLES);
        for (Polygon t : polygons) if (t.vertices.size() == 3) sendData(color, selectedOnly, t);
        glEnd();
        glBegin(GL_QUADS);
        for (Polygon t : polygons) if (t.vertices.size() == 4) sendData(color, selectedOnly, t);
        glEnd();
    }

    private void sendData(float color, boolean selectedOnly, Polygon t) {
        if (selectedOnly && !t.isSelected()) return;
        for (Vertex v : t.vertices) {
            glColor3f(color, color, color);
            Vec3f normal = v.normal;
            glNormal3f(normal.x, normal.y, normal.z);
            glVertex3f(v.pos.x, v.pos.y, v.pos.z);
        }
    }

    private void renderLines(float color, boolean selectedOnly) {
        setCam();
        glBegin(GL_LINES);
        a:
        for (Polygon t : polygons) {
            for (int i = 0; i < t.vertices.size(); i++) {
                Vertex a = t.vertices.get(i);
                Vertex b = t.vertices.get(i == t.vertices.size() - 1 ? 0 : i + 1);
                if (!selectedOnly || (a.selected || b.selected)) {
                    glColor3f(color, color, color);
                    glNormal3f(a.normal.x, a.normal.y, a.normal.z);
                    glVertex3f(a.pos.x, a.pos.y, a.pos.z);
                    glColor3f(color, color, color);
                    glNormal3f(b.normal.x, b.normal.y, b.normal.z);
                    glVertex3f(b.pos.x, b.pos.y, b.pos.z);
                }
            }
        }
        glEnd();
    }

    private void renderDots(float color, boolean selectedOnly) {
        DDDUtils.setCam(cam);
        setCam();
        glBegin(GL_POINTS);
        for (Vertex v : vertices)
            if (!selectedOnly || v.selected) {
                glColor3f(color, color, color);
                Vec3f normal = v.normal;
                glNormal3f(normal.x, normal.y, normal.z);
                glVertex3f(v.pos.x, v.pos.y, v.pos.z);
            }
        glEnd();
        if (showNormals) {
            glBegin(GL_LINES);
            for (Vertex v : vertices)
                if (!selectedOnly || v.selected) {
                    glColor3f(color, color, color);
                    Vec3f normal = v.normal;
                    glVertex3f(v.pos.x, v.pos.y, v.pos.z);
                    Vec3f p2 = v.pos.add(normal);
                    glVertex3f(p2.x, p2.y, p2.z);
                }
            glEnd();
        }
    }

    private void drawText() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, w, h, 0, 1, -1);

        glMatrixMode(GL11.GL_MODELVIEW);
        glLoadIdentity();
        org.newdawn.slick.Color c = new Color(0.1f, 0.1f, 0.2f, .5f);

        for (Map.Entry<String, ? extends MenuEntry> entry : currentMenus.entrySet()) {
            int i = letters.indexOf(entry.getKey());
            int y = i * 20;
            if (i >= 5) y += 20;
            if (i >= 10) y += 20;
            font.drawString(10, 130 + y, letters.get(i), c);
            //MenuEntry menuEntry = currentMenu.menu.get(i);
            //if (menuEntry != null) {
            TextureImpl.unbind();
            if (entry.getValue().pressed) {
                font2.drawString(30, 130 + y, entry.getValue().name, c);
            } else {
                font.drawString(30, 130 + y, entry.getValue().name, c);
            }
        }
        font.drawString(10, 100, "magnification: X " + magnifier, c);
        TextureImpl.unbind();
//        font.drawString(10, 70, "pool size: " + treePool.firstFree, c);
//        TextureImpl.unbind();
    }

    private void initGL(int width, int height) throws LWJGLException {
        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        Display.setParent(canvas);

        canvas.setSize(w, h);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Wavefront obj", "obj"));
                chooser.showOpenDialog(frame);
                File file = chooser.getSelectedFile();
                if (file == null) return;
                loadFromFile(file.getAbsolutePath());
            }
        });
        menuItem.setText("Open");
        menu.add(menuItem);

        menuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setApproveButtonText("Save");
                chooser.setFileFilter(new FileNameExtensionFilter("Wavefront obj", "obj"));
                chooser.showOpenDialog(frame);
                File file = chooser.getSelectedFile();
                if (file == null) return;
                String path = file.getAbsolutePath();
                if (!path.endsWith(".obj")) path += ".obj";
                writeToFile(path);
            }
        });
        menuItem.setText("Save as");
        menu.add(menuItem);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Display.create();
                    canvas.requestFocus();
                    firstInit();
                } catch (LWJGLException e) {
                    //TODO error handling
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCam() {
        glMatrixMode(GL11.GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45.0f, (float) w / h, magnifier, 1000.0f * magnifier);
        DDDUtils.setCam(cam);
    }

    private void drawArrow(Quaternionf arrowSceneSpaceRot, Vec3f arrowSceneSpacePos) {
        glTranslatef(arrowSceneSpacePos.x, arrowSceneSpacePos.y, arrowSceneSpacePos.z);
        DDDUtils.multMatrix(arrowSceneSpaceRot);
        Primitives.drawCube(true, 0.5f, magnifier);
    }

    private static class Key {
        public boolean ctrl;
        public boolean alt;
        public boolean shift;

        int k;
        int v;

        private Key(int k) {
            this.k = k;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "ctrl=" + ctrl +
                    ", alt=" + alt +
                    ", shift=" + shift +
                    ", k=" + k +
                    ", v=" + v +
                    '}';
        }
    }

    public void splitEdge(Vertex a, Vertex b, Vec3f pos) {
        Vertex newVertex = new Vertex(pos);
        vertices.add(newVertex);
        List<Polygon> newPolygons = list();
        for (Iterator<Polygon> iterator = polygons.iterator(); iterator.hasNext(); ) {
            Polygon polygon = iterator.next();
            if (!polygon.vertices.contains(a) && !polygon.vertices.contains(a)) continue;
            iterator.remove();
            Vertex old = other(polygon, a, b);
            newPolygons.add(new Polygon(a, old, newVertex));
            newPolygons.add(new Polygon(b, newVertex, old));//TODO fix - bad split of quads?
        }
        polygons.addAll(newPolygons);
    }

    private Vertex other(Polygon t, Vertex i, Vertex j) {
        for (int k = 0; k < t.vertices.size(); k++) {
            Vertex v = t.vertices.get(k);
            if (v != i && v != j) return v;
        }
        return null;
    }

    public void replace(Vertex from, Vertex to) {
        for (Polygon polygon : polygons) Collections.replaceAll(polygon.vertices, from, to);
        cleanUp();
    }

    public void cleanUp() {
        for (Vertex v : vertices) {
            v.dirty = false;
        }

        for (Iterator<Polygon> iterator = polygons.iterator(); iterator.hasNext(); ) {
            Polygon polygon = iterator.next();
            //remove repeating vertices
            Vertex last = null;
            for (Iterator<Vertex> iterator1 = polygon.vertices.iterator(); iterator1.hasNext(); ) {
                Vertex cur = iterator1.next();
                if (cur == last) iterator1.remove();
                last = cur;
            }
            if (polygon.vertices.get(0) == polygon.vertices.get(polygon.vertices.size() - 1)) polygon.vertices.remove(polygon.vertices.size() - 1);
            //remove < 3 polys
            if (polygon.vertices.size() < 3) iterator.remove();

            for (Vertex i : polygon.vertices) i.dirty = true;
        }

        //remove not used vertex
        for (int i = 0; i < vertices.size(); i++) {
            if (!vertices.get(i).dirty) {
                vertices.set(i, vertices.get(vertices.size() - 1));
                vertices.remove(vertices.size()  - 1);
            }
        }
    }

    long lastSave;
    public void makeTickedHistory() {
        if (System.currentTimeMillis() - lastSave > 3000) {
            makeHistory();
            lastSave = System.currentTimeMillis();
        }
    }

    public void makeHistory() {
        //TODO fix - redundant work if undoed
        //TODO anyway - make immutable
        List l = getCopy(polygons, vertices);
        putInHistory((List)l.get(0), (List)l.get(1));
    }

    private void putInHistory(List<Polygon> polygons, List<Vertex> vertices) {
        while(historyPosition < historyVertices.size() - 1) {
            historyVertices.remove(historyVertices.size() - 1);
            historyPolygons.remove(historyPolygons.size() - 1);
        }

        historyPosition++;
        historyVertices.add(historyPosition, vertices);
        historyPolygons.add(historyPosition, polygons);

        if (historyVertices.size() > 5) {
            historyVertices.remove(0);
            historyPolygons.remove(0);
            historyPosition--;
        }
    }

    public static List getCopy(List<Polygon> pp, List<Vertex> vv) {
        List<Vertex> vertices = new ArrayList<Vertex>();
        Map<Vertex, Vertex> mm = map();
        for (Vertex v : vv) {
            Vertex copy = v.copy();
            vertices.add(copy);
            mm.put(v, copy);
        }

        List<Polygon> polygons = list();
        for (Polygon p : pp) {
            polygons.add(p.copy(mm));
        }
        return list(polygons, vertices);
    }

    public void undo() {
        if (historyPosition == 0) return;

        historyPosition--;
        List l = getCopy(historyPolygons.get(historyPosition), historyVertices.get(historyPosition));
        polygons = (List)l.get(0);
        vertices = (List)l.get(1);
    }

    public void redo() {
        if (historyPosition == historyVertices.size() - 1) return;
        historyPosition++;
        List l = getCopy(historyPolygons.get(historyPosition), historyVertices.get(historyPosition));
        polygons = (List)l.get(0);
        vertices = (List)l.get(1);
    }

    public int historyPosition = -1;
    public List<List<Vertex>> historyVertices = list();
    public List<List<Polygon>> historyPolygons = list();

}

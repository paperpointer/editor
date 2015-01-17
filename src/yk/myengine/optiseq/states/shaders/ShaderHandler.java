package yk.myengine.optiseq.states.shaders;

import yk.myengine.optiseq.AbstractState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.Util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Yuri Kravchik Date: 2/11/2007 Time: 11:29:31
 */
public class ShaderHandler extends AbstractState {
    public static ShaderHandler currentShader;

    public ShaderHandler() {
    }


    public ShaderHandler(String v, String f) {
        createProgram(v, f);
    }



    private static ByteBuffer getProgramCode(final String filename) {
        final ClassLoader fileLoader = ShaderHandler.class.getClassLoader();
        InputStream fileInputStream = fileLoader.getResourceAsStream(filename);
        byte[] shaderCode = null;

        try {
            if (fileInputStream == null) {
                fileInputStream = new FileInputStream(filename);
            }
            final DataInputStream dataStream = new DataInputStream(fileInputStream);
            dataStream.readFully(shaderCode = new byte[fileInputStream.available()]);
            fileInputStream.close();
            dataStream.close();
            final ByteBuffer shaderPro = BufferUtils.createByteBuffer(shaderCode.length);

            shaderPro.put(shaderCode);
            shaderPro.flip();

            return shaderPro;
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void printLogInfo(final int obj) {
        final IntBuffer iVal = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(
                obj,
                ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB,
                iVal);

        final int length = iVal.get();
        System.out.println("Info log length:" + length);
        if (length > 0) {
            // We have some info we need to output.
            final ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            final byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            final String out = new String(infoBytes);

            System.out.println("Info log:\n" + out);
        }

        Util.checkGLError();
    }

    public static int createFragmentShader(final ByteBuffer program) {
        final int fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fs, program);
        GL20.glCompileShader(fs);
        printLogInfo(fs);
        return fs;
    }

    public static int createVertexShader(final ByteBuffer program) {
        final int vs = ARBShaderObjects
                .glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        ARBShaderObjects.glShaderSourceARB(vs, program);
        ARBShaderObjects.glCompileShaderARB(vs);
        printLogInfo(vs);
        return vs;
    }

    public static ByteBuffer getBufferedString(final String s) {
        final ByteBuffer bb = BufferUtils.createByteBuffer(s.length() + 1);
        bb.put(s.getBytes());
        bb.put((byte) 0);
        bb.rewind();
        return bb;
    }

    public static int getUniformLocation(final int program, final ByteBuffer attributeName) {
        return GL20.glGetUniformLocation(program, attributeName);
    }

    private int program;
    private final List<UniformVariable> uniforms = new ArrayList<UniformVariable>();
    public final Map<String, VertexAttrib> vertexAttribs = new HashMap<String, VertexAttrib>();

    public void initVariables() {
        for (final UniformVariable u4f : uniforms) {
            u4f.initForProgram(program);
        }
        for (final VertexAttrib a : vertexAttribs.values()) {
            a.initForProgram(program);
        }
    }

    public void addVariables(final UniformVariable... variables) {
        for (final UniformVariable v : variables) {
            uniforms.add(v);
        }
    }

    public void addVertexAttrib(final VertexAttrib attrib) {
        vertexAttribs.put(attrib.getName(), attrib);
    }

    public void createProgram(String vss, String fss) {
        createProgram(new String[]{vss}, new String[]{fss});
    }

    public void createProgram(final String[] vss, final String[] fss) {
        program = ARBShaderObjects.glCreateProgramObjectARB();
        program = GL20.glCreateProgram();
        for (final String s : vss) {
            final int shader = createVertexShader(getProgramCode(s));
            GL20.glAttachShader(program, shader);
            GL20.glDeleteShader(shader);
        }
        for (final String s : fss) {
            final int shader = createFragmentShader(getProgramCode(s));
            GL20.glAttachShader(program, shader);
            GL20.glDeleteShader(shader);
        }
        GL20.glLinkProgram(program);
        printLogInfo(program);

        initVariables();
    }

    public void createFromSrc(String vsrc, String fsrc) {
        program = ARBShaderObjects.glCreateProgramObjectARB();
        program = GL20.glCreateProgram();

        int shader = createVertexShader(stringToBuffer(vsrc));
        GL20.glAttachShader(program, shader);
        GL20.glDeleteShader(shader);

        shader = createFragmentShader(stringToBuffer(fsrc));
        GL20.glAttachShader(program, shader);
        GL20.glDeleteShader(shader);

        GL20.glLinkProgram(program);
        printLogInfo(program);

        initVariables();
    }

    private ByteBuffer stringToBuffer(String vsrc) {
        byte[] progBytes = vsrc.getBytes();
        final ByteBuffer progBuf = BufferUtils.createByteBuffer(progBytes.length);
        progBuf.put(progBytes);
        progBuf.flip();
        return progBuf;
    }

    public void disable() {
        GL20.glUseProgram(0);
        currentShader = null;
    }

    public void enable() {
        GL20.glUseProgram(program);
        for (final UniformVariable u4f : uniforms) {
            u4f.plug();
        }
        currentShader = this;
    }

    public VertexAttrib getVertexAttrib(final String name) {
        return vertexAttribs.get(name);
    }

    public static void setShader(ShaderHandler handler) {
        if (currentShader != handler) {
            if (currentShader != null) currentShader.disable();
            currentShader = handler;
            if (currentShader != null) currentShader.enable();
        }
    }

}

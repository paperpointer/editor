package yk.myengine.visualrealm.utils;

import org.lwjgl.BufferUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

/**
 * Created by: Yuri Kravchik
 * Date: 22 11 2007
 * Time: 16:11:34
 */
public class TGALoader {

    public static ByteBuffer createRGBA(String tgaFileName) {
        try {
            File f = new File(tgaFileName);
            FileInputStream fio = new FileInputStream(f);
            int readed;
            DataInputStream dis = new DataInputStream(fio);

            int width;
            int height;

            dis.skip(12);

            width = dis.readUnsignedByte();
            width |= dis.readUnsignedByte() << 8;
            System.out.println("Width: " + width);
            height = dis.readUnsignedByte();
            height |= dis.readUnsignedByte() << 8;
            System.out.println("Height: " + height);

            dis.skip(2);

            //todo: move to the java NIO

            ByteBuffer result = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = 0; y < height / 2/*!!*/; y++) {
                for (int x = 0; x < width; x++) {
                    int offset = ((height - y - 1) * width + x) * 4;
                    result.put(offset, dis.readByte());
                    result.put(offset + 1, dis.readByte());
                    result.put(offset + 2, dis.readByte());
                    result.put(offset + 3, dis.readByte());
                }
            }
            result.rewind();
            fio.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        createRGBA("defaultfont.tga");
    }
}

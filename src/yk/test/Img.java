package yk.test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Kravchik Yuri
 * Date: 02.07.2012
 * Time: 9:14 PM
 */
public class Img {
    public final BufferedImage bi;
    byte[] bytes;

    public Img(BufferedImage bi) {
        this.bi = bi;
        bytes = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    }

    public void setR(int x, int y, float color) {
        int i = 0;
        set(x, y, i, color);
    }

    public float getR(int x, int y) {
        return get(x, y, 0);
    }

    public float getG(int x, int y) {
        return get(x, y, 1);
    }

    public float getB(int x, int y) {
        return get(x, y, 2);
    }

    public float get(int x, int y, int i) {
        return (float)convert(bytes[index(x, y, bi.getWidth(), 3) + i]) / 255;
    }

    public void set(int x, int y, int i, float color) {
        bytes[index(x, y, bi.getWidth(), 3) + i] = convert(color * 255);
    }

    public float mean(int x, int y) {
        float result = 0;
        for (int i = 0; i < 3; i++) {
            result += get(x, y, i);
        }
        return result / 3;
    }


    public static int[] get(byte[] data, int x, int y, int picW) {
        return new int[]{
                convert(data[index(x, y, picW, 3) + 0]),
                convert(data[index(x, y, picW, 3) + 1]),
                convert(data[index(x, y, picW, 3) + 2])
                };
    }

    private static int convert(byte b) {
        return b >= 0 ? b : (int)b+ 256;
    }

    public static int index(int x, int y, int picW, int step) {
        return (x + y * picW) * step;
    }

    private static byte convert(float f) {
        int i = (int) f;
        return i < 128 ? (byte) i : (byte)(i - 256);
    }


}

/**
 * File SpeedTest.java
 * @author Yuri Kravchik
 * Created 12.01.2008
 */
package yk.myengine;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

/**
 * SpeedTest
 *
 * @author Yuri Kravchik Created 12.01.2008
 */
public class SpeedTest {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        testJavaBuffers();
    }

    public static long testByteArray() {
        final long startTime = System.nanoTime();
        final byte[] bBuffer = new byte[100000];

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 100000; j++) {
                bBuffer[j] = (byte) j;
            }
        }
        long result = System.nanoTime() - startTime;
        System.out.println(bBuffer[3]);
        return result;
    }

    public static long testByteBuffer() {
        final long startTime = System.nanoTime();
        final ByteBuffer bBuffer = BufferUtils.createByteBuffer(100000);

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 100000; j++) {
                bBuffer.put((byte) j);
            }
            bBuffer.rewind();

        }

        return System.nanoTime() - startTime;
    }

    public static long testByteBufferAddressed() {
        final long startTime = System.nanoTime();
        final ByteBuffer bBuffer = BufferUtils.createByteBuffer(100000);

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 100000; j++) {
                bBuffer.put(j, (byte) j);
            }

        }

        return System.nanoTime() - startTime;
    }

    public static void testJavaBuffers() {
        System.out.println("Testing byte buffer:");
        for (int i = 0; i < 10; i++) {
            System.out.println("Elapsed time: " + testByteBuffer() / 1000000);
        }

        System.out.println("\nTesting byte buffer addressed:");
        for (int i = 0; i < 10; i++) {
            System.out.println("Elapsed time: " + testByteBufferAddressed() / 1000000);
        }

        System.out.println("\nTesting byte array:");
        for (int i = 0; i < 10; i++) {
            System.out.println("Elapsed time: " + testByteArray() / 1000000);
        }
    }

    public static long testOperationMul() {
        final long startTime = System.nanoTime();
        int a = 1;

        for (int j = 0; j < 10000000; j++) {
            a = a * j;
        }

        return System.nanoTime() - startTime;
    }

    public static long testOperationPlus() {
        final long startTime = System.nanoTime();
        int a = 1;

        for (int j = 0; j < 10000000; j++) {
            a = a + j;
        }

        return System.nanoTime() - startTime;
    }

    public static long testOperationSqrt() {
        final long startTime = System.nanoTime();
        double a = 1;

        for (float j = 0; j < 10000000; j++) {
            a = Math.sqrt(j);
        }

        return System.nanoTime() - startTime;
    }

    public static void testOperators() {
        System.out.println("Testing plus:");
        for (int i = 0; i < 10; i++) {
            System.out.println("Elapsed time: " + testOperationPlus() / 1000000);
        }
        System.out.println("Testing mul:");
        for (int i = 0; i < 10; i++) {
            System.out.println("Elapsed time: " + testOperationMul() / 1000000);
        }
        System.out.println("Testing sqrt:");
        for (int i = 0; i < 10; i++) {
            System.out.println("Elapsed time: " + testOperationSqrt() / 1000000);
        }
    }

}

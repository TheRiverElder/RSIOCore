package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.compile.DataType;

public class ByteArrays {

    //region read bytes

    public static long read(byte[] bytes, int startIndex, int byteCount) {
        long value = 0;
        for (int i = 0; i < byteCount; i++) {
            value = (value << i * Byte.SIZE) | Byte.toUnsignedInt(bytes[startIndex++]);
        }
        return value;
    }

    public static byte read8(byte[] bytes, int startIndex) {
        return bytes[startIndex];
    }

    public static short read16(byte[] bytes, int startIndex) {
        int value = 0;
        for (int i = 0; i < 2; i++) {
            value = (value << i * Byte.SIZE) | Byte.toUnsignedInt(bytes[startIndex++]);
        }
        return (short) value;
    }

    public static int read32(byte[] bytes, int startIndex) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value = (value << i * Byte.SIZE) | Byte.toUnsignedInt(bytes[startIndex++]);
        }
        return value;
    }

    public static long read64(byte[] bytes, int startIndex) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = (value << i * Byte.SIZE) | Byte.toUnsignedInt(bytes[startIndex++]);
        }
        return value;
    }

    //endregion

    //region write bytes

    public static void write(byte[] bytes, int startIndex, int byteCount, long value) {
        for (int i = 0; i < byteCount; i++) {
            bytes[startIndex + i] = (byte) ((value >>> (byteCount - 1 - i) * Byte.SIZE) & 0xFF);
        }
    }

    public static byte write8(byte[] bytes, int startIndex, byte value) {
        return bytes[startIndex] = value;
    }

    public static short write16(byte[] bytes, int startIndex, short value) {
        bytes[startIndex] = (byte) (value >>> Byte.SIZE);
        bytes[startIndex + 1] = (byte) (value & 0xFF);
        return value;
    }

    public static int write32(byte[] bytes, int startIndex, int value) {
        for (int i = 0; i < 4; i++) {
            bytes[startIndex + i] = (byte) ((value >>> (3 - i) * Byte.SIZE) & 0xFF);
        }
        return value;
    }

    public static long write64(byte[] bytes, int startIndex, long value) {
        for (int i = 0; i < 8; i++) {
            bytes[startIndex + i] = (byte) ((value >>> (3 - i) * Byte.SIZE) & 0xFF);
        }
        return value;
    }

    //endregion

}

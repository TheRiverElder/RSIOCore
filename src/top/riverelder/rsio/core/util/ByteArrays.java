package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.compile.DataType;

public class ByteArrays {

    public static int readInt(byte[] bytes, int startIndex) {
        int value = 0;
        for (int i = 0; i < DataType.INTEGER.length; i++) {
            value = (value << i * Byte.SIZE) | Byte.toUnsignedInt(bytes[startIndex++]);
        }
        return value;
    }

    public static void writeInt(byte[] bytes, int startIndex, int value) {
        for (int i = Integer.BYTES - 1; i >= 0; i--) {
            bytes[startIndex++] = (byte) ((value >>> i * Byte.SIZE) & 0b11111111);
        }
    }

}

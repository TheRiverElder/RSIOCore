package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.compile.DataType;

public class BytesReader {

    private final byte[] bytes;
    private int cursor = 0;

    public BytesReader(byte[] bytes) {
        this.bytes = bytes;
    }

    public boolean hasMore(int length) {
        return cursor < bytes.length;
    }

    public boolean hasByte() {
        return hasMore(1);
    }

    public boolean hasInt() {
        return hasMore(DataType.INTEGER.length);
    }

    public boolean hasDecimal() {
        return hasMore(DataType.DECIMAL.length);
    }

    public byte readByte() {
        return bytes[cursor++];
    }

    public int readInt() {
        int value = ByteArrays.readInt(bytes, cursor);
        cursor += Integer.BYTES;
        return value;
    }

    public double readDecimal() {
        int value = 0;
        for (int i = 0; i < DataType.DECIMAL.length; i++) {
            value = (value << i * Byte.SIZE) | bytes[cursor++];
        }
        return Double.longBitsToDouble(value);
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }
}

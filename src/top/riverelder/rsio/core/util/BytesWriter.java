package top.riverelder.rsio.core.util;

import java.util.ArrayList;
import java.util.List;

public class BytesWriter {

    private final List<Byte> bytes;

    public BytesWriter() {
        bytes = new ArrayList<>();
    }

    public BytesWriter writeByte(byte value) {
        bytes.add(value);
        return this;
    }

    public BytesWriter writeInteger(int value) {
        for (int i = Integer.BYTES - 1; i >= 0; i--) {
            bytes.add((byte) ((value >>> i * Byte.SIZE) & 0b11111111));
        }
        return this;
    }

    public BytesWriter writeIntegerAt(int startIndex, int value) {
        for (int i = Integer.BYTES - 1; i >= 0; i--) {
            bytes.set(startIndex++, (byte) ((value >>> i * Byte.SIZE) & 0b11111111));
        }
        return this;
    }

    public BytesWriter writeDecimal(double value) {
        long v = Double.doubleToLongBits(value);
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            bytes.add((byte) ((v >>> i * Byte.SIZE) & 0b11111111));
        }
        return this;
    }

    public byte[] toBytes() {
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes.get(i);
        }
        return result;
    }

    public int size() {
        return bytes.size();
    }
}

package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.compile.DataType;

import java.util.Arrays;

public class Memory {

    private byte[] data;
    private int pointer;

    public Memory(byte[] data) {
        this.data = data;
    }

    public Memory(int size) {
        this.data = new byte[size];
    }

    public Memory at(int pointer) {
        this.pointer = pointer;
        return this;
    }

    public Memory writeByte(byte value) {
        data[pointer] = value;
        return this;
    }

    public Memory writeInt(int value) {
        ByteArrays.writeInt(data, pointer, value);
        return this;
    }

    public Memory writeDecimal(double value) {
        long v = Double.doubleToLongBits(value);
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            data[pointer++] = (byte) ((v >>> i * Byte.SIZE) & 0b11111111);
        }
        return this;
    }

    public byte readByte() {
        return data[pointer];
    }

    public int readInt() {
        int value = ByteArrays.readInt(data, pointer);
        pointer += Integer.BYTES;
        return value;
    }

    public double readDecimal() {
        long value = 0;
        for (int i = 0; i < DataType.DECIMAL.length; i++) {
            value = (value << i * Byte.SIZE) | Byte.toUnsignedLong(data[pointer++]);
        }
        return Double.longBitsToDouble(value);
    }

    public void print() {
        System.out.println(Arrays.toString(data));
    }

}

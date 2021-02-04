package top.riverelder.rsio.core.instruction;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public class Push extends Instruction {

    public static final byte HEAD = 0x01;

    private final int dataSize;
    private long data;

    public Push(int dataSize, long data) {
        this.dataSize = dataSize;
        this.data = data;
    }

    public Push(int dataSize, boolean data) {
        this(dataSize, Integer.toUnsignedLong(data ? 1 : 0));
    }

    public Push(int dataSize, byte data) {
        this(dataSize, Byte.toUnsignedLong(data));
    }

    public Push(int dataSize, short data) {
        this(dataSize, Short.toUnsignedLong(data));
    }

    public Push(int dataSize, int data) {
        this(dataSize, Integer.toUnsignedLong(data));
    }

    public Push(int dataSize, float data) {
        this(dataSize, Integer.toUnsignedLong(Float.floatToIntBits(data)));
    }

    public Push(int dataSize, double data) {
        this(dataSize, Double.doubleToLongBits(data));
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {

    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append("  push_").append(dataSize).append(' ').append(String.format("%" + (dataSize * 2) +"x", data)).append("H;\n");
    }
}

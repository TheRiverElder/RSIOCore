package top.riverelder.rsio.core.util;

public class Convert {

    // 避免long转为其它长度的数据时正负失真的问题
    public static long convert(long value, int targetLength) {
        switch (targetLength) {
            case 1: return Byte.toUnsignedLong((byte) value);
            case 2: return Short.toUnsignedLong((short) value);
            case 4: return Integer.toUnsignedLong((int) value);
            case 8: return value;
        }
        return value;
    }

}

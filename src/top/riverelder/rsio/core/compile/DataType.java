package top.riverelder.rsio.core.compile;

public class DataType {

    public static final int ADDRESS_LENGTH = 4;

    public static final DataType STRING = new DataType("string", 4, false, 4);
    public static final DataType DECIMAL = new DataType("double", 8, true, 3);
    public static final DataType INTEGER = new DataType("int", 4, false, 2);
    public static final DataType BOOLEAN = new DataType("bool", 1, false, 1);
    public static final DataType VOID = new DataType("void", 0, false, 0);


    public static DataType getHigher(DataType t1, DataType t2) {
        return t1.level > t2.level ? t1 : t2;
    }


    public final String name;
    public final int length;
    // code 有四位，第一位为0代表是整型数，为1代表是浮点数，后三位代表所占字节数
    public final int code;
    public final boolean isFloat;
    public final int level;

    public DataType(String name, int length, boolean isFloat, int level) {
        this.name = name;
        this.length = length;
        this.code = (isFloat ? 0b1000 : 0) | length;
        this.isFloat = isFloat;
        this.level = level;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}

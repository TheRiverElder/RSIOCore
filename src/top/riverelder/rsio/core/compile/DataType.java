package top.riverelder.rsio.core.compile;

public class DataType {

    public static final DataType STRING = new DataType("string", 4);
    public static final DataType DECIMAL = new DataType("double", 8);
    public static final DataType INTEGER = new DataType("int", 4);
    public static final DataType BOOLEAN = new DataType("bool", 1);
    public static final DataType VOID = new DataType("void", 0);




    public final String name;
    public final int length;

    public DataType(String name, int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}

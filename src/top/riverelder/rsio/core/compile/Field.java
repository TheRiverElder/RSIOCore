package top.riverelder.rsio.core.compile;

public class Field {
    public final String name;
    public final int position;
    public final DataType type;

    public Field(String name, int position, DataType type) {
        this.name = name;
        this.position = position;
        this.type = type;
    }
}

package top.riverelder.rsio.core.compile;

public class Field {
    public final String name;
    public final int position;
    public final DataType type;
    public final boolean isConstant;

    public Field(String name, int position, DataType type, boolean isConstant) {
        this.name = name;
        this.position = position;
        this.type = type;
        this.isConstant = isConstant;
    }

    @Override
    public String toString() {
        return String.format("%s : %s @ %d", name, type.name, position);
    }
}

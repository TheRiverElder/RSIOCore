package top.riverelder.rsio.core.compile;

public interface CompileEnvironment {

    boolean hasParent();

    CompileEnvironment getParent();

    Field createField(String name, DataType type, boolean isConstant);

    Field getField(String name);

    void putDataType(DataType dataType);

    DataType getDataType(String name);

    int countLabel();
}

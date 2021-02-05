package top.riverelder.rsio.core.compile;

public interface CompileEnvironment {

    Field createField(String name, DataType type);

    Field getField(String name);

    int countLabel();
}

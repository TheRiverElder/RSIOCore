package top.riverelder.rsio.core.compile;

public class IndependentCompileEnvironment implements CompileEnvironment {
    @Override
    public Field createField(String name, DataType type) {
        return null;
    }

    @Override
    public Field getField(String name) {
        return null;
    }

    @Override
    public int countLabel() {
        return 0;
    }
}

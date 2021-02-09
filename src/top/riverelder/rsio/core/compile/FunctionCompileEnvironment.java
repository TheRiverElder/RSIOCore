package top.riverelder.rsio.core.compile;

public class FunctionCompileEnvironment extends NestedCompileEnvironment {

    protected int labelCounter = 0;

    public FunctionCompileEnvironment(CompileEnvironment parent) {
        super(parent);
    }

    @Override
    public void putDataType(DataType dataType) {
        if (hasParent()) {
            getParent().putDataType(dataType);
        }
    }

    @Override
    public DataType getDataType(String name) {
        return hasParent() ? getParent().getDataType(name) : null;
    }

    @Override
    public int countLabel() {
        return labelCounter++;
    }
}

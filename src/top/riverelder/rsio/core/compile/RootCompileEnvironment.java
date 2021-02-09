package top.riverelder.rsio.core.compile;

import java.util.HashMap;
import java.util.Map;

public class RootCompileEnvironment extends CompileEnvironmentBase {

    protected int labelCounter = 0;
    protected final Map<String, DataType> dataTypes = new HashMap<>();

    public RootCompileEnvironment() {
    }

    @Override
    public boolean hasParent() {
        return false;
    }

    @Override
    public CompileEnvironment getParent() {
        return null;
    }

    @Override
    public void putDataType(DataType dataType) {
        dataTypes.put(dataType.name, dataType);
    }

    @Override
    public DataType getDataType(String name) {
        return dataTypes.get(name);
    }

    @Override
    public int countLabel() {
        return labelCounter++;
    }
}

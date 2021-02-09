package top.riverelder.rsio.core.compile;

import com.sun.istack.internal.NotNull;

public class NestedCompileEnvironment extends CompileEnvironmentBase {

    protected final CompileEnvironment parent;

    public NestedCompileEnvironment(@NotNull CompileEnvironment parent) {
        this.parent = parent;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public CompileEnvironment getParent() {
        return parent;
    }

    public int countLabel() {
        return parent.countLabel();
    }

    @Override
    public void putDataType(DataType dataType) {
        if (hasParent()) {
            getParent().putDataType(dataType);
        }
    }

    public DataType getDataType(String name) {
        return hasParent() ? parent.getDataType(name) : null;
    }
}

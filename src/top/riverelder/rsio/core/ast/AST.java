package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.IToSource;

import java.util.List;

public abstract class AST implements IToSource {
    private final int position;

    public AST(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    abstract public DataType getDataType(CompileEnvironment env);

    abstract public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException;
}

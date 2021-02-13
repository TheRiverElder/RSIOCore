package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class Statement extends AST {

    private final AST valuable;

    public Statement(int position, AST valuable) {
        super(position);
        this.valuable = valuable;
    }

    @Override
    public DataType getDataType(CompileEnvironment env) throws RSIOCompileException {
        return DataType.VOID;
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        valuable.toAssemble(output, env);
        DataType dataType = valuable.getDataType(env);
        if (dataType.length != 0) {
            output.add("  pop." + dataType.length);
        }
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write(valuable).write(';');
    }
}

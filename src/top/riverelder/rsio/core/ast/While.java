package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.AssembleUtils;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class While extends AST {

    private final AST condition;
    private final AST body;

    public While(int position, AST condition, AST body) {
        super(position);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public DataType getDataType(CompileEnvironment env) throws RSIOCompileException {
        return body.getDataType(env);
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        String startLabel = String.format("L%d", env.countLabel());
        String endLabel = String.format("L%d", env.countLabel());
        output.add(startLabel + ":");
        condition.toAssemble(output, env);
        AssembleUtils.checkAndCast(output, condition.getDataType(env), DataType.BOOLEAN);
        output.add("  izj " + endLabel);
        body.toAssemble(output, env);
        output.add("  jmp " + startLabel);
        output.add(endLabel + ":");
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write("while (").write(condition).write(") ").write(body);
    }
}

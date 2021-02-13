package top.riverelder.rsio.core.ast;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.AssembleUtils;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class If extends AST {

    private final AST condition;
    private final AST ifTrueValue;
    private final AST ifFalseValue;

    public If(int position, @NotNull AST condition,@NotNull AST ifTrueValue, @Nullable AST ifFalseValue) {
        super(position);
        this.condition = condition;
        this.ifTrueValue = ifTrueValue;
        this.ifFalseValue = ifFalseValue;
    }

    @Override
    public DataType getDataType(CompileEnvironment env) throws RSIOCompileException {
        if (ifFalseValue == null) return ifTrueValue.getDataType(env);
        return DataType.getHigher(ifTrueValue.getDataType(env), ifFalseValue.getDataType(env));
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write("if (").write(condition).write(") ").write(ifTrueValue);
        if (ifFalseValue != null) {
            builder.write(" else ").write(ifFalseValue);
        }
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        condition.toAssemble(output, env);
        AssembleUtils.checkAndCast(output, condition.getDataType(env), DataType.BOOLEAN);
        if (ifFalseValue != null) {
            String falseStartLabel = String.format("L%d", env.countLabel());
            String endLabel = String.format("L%d", env.countLabel());
            output.add("  izj " + falseStartLabel);
            ifTrueValue.toAssemble(output, env);
            output.add("  jmp " + endLabel);
            output.add(falseStartLabel + ":");
            ifFalseValue.toAssemble(output, env);
            output.add(endLabel + ":");
        } else {
            String endLabel = String.format("L%d", env.countLabel());
            output.add("  izj " + endLabel);
            ifTrueValue.toAssemble(output, env);
            output.add(endLabel + ":");
        }
    }
}

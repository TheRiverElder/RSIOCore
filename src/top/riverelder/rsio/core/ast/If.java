package top.riverelder.rsio.core.ast;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;
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
    public DataType getDataType(NestedCompileEnvironment env) {
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
    public void toAssemble(List<String> output, NestedCompileEnvironment env) throws RSIOCompileException {
        condition.toAssemble(output, env);
        if (ifFalseValue != null) {
            String falseStartLabel = String.format("S%d_N%d_L%d", env.getDepth(), env.getNumber(), env.countLabel());
            String endLabel = String.format("S%d_N%d_L%d", env.getDepth(), env.getNumber(), env.countLabel());
            output.add("  izj " + falseStartLabel);
            ifTrueValue.toAssemble(output, env);
            output.add("  jmp " + endLabel);
            output.add(falseStartLabel + ":");
            ifFalseValue.toAssemble(output, env);
            output.add(endLabel + ":");
        } else {
            String endLabel = String.format("S%d_N%d_L%d", env.getDepth(), env.getNumber(), env.countLabel());
            output.add("  izj " + endLabel);
            ifTrueValue.toAssemble(output, env);
            output.add(endLabel + ":");
        }
    }
}

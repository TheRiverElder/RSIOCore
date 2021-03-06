package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.AssembleUtils;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class TrinaryExpression extends AST {

    private final AST condition;
//    private final Operator operator;
    private final AST trueValue;
    private final AST falseValue;

    public TrinaryExpression(int position, AST condition, AST trueValue, AST falseValue) {
        super(position);
        this.condition = condition;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write('(').write(condition).write(") ? (").write(trueValue).write(") : (").write(falseValue).write(')');
    }

    @Override
    public DataType getDataType(CompileEnvironment env) throws RSIOCompileException {
        return DataType.getHigher(trueValue.getDataType(env), falseValue.getDataType(env));
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        DataType trueValueDataType = trueValue.getDataType(env);
        DataType falseValueDataType = falseValue.getDataType(env);
        DataType finalDataType = DataType.getHigher(trueValueDataType, falseValueDataType);

        condition.toAssemble(output, env);
        AssembleUtils.checkAndCast(output, condition.getDataType(env), DataType.BOOLEAN);

        String falseStartLabel = String.format("L%d", env.countLabel());
        String endLabel = String.format("L%d", env.countLabel());

        output.add("  izj " + falseStartLabel);

        trueValue.toAssemble(output, env);
        AssembleUtils.checkAndCast(output, trueValueDataType, finalDataType);

        output.add("  jmp " + endLabel);

        output.add(falseStartLabel + ":");
        falseValue.toAssemble(output, env);
        AssembleUtils.checkAndCast(output, falseValueDataType, finalDataType);

        output.add(endLabel + ":");
    }
}

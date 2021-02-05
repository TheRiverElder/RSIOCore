package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
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
    public DataType getDataType(NestedCompileEnvironment env) {
        return DataType.getHigher(trueValue.getDataType(env), falseValue.getDataType(env));
    }

    @Override
    public void toAssemble(List<String> output, NestedCompileEnvironment env) throws RSIOCompileException {
        DataType trueValueDataType = trueValue.getDataType(env);
        DataType falseValueDataType = falseValue.getDataType(env);
        DataType finalDataType = DataType.getHigher(trueValueDataType, falseValueDataType);

        condition.toAssemble(output, env);

        String falseStartLabel = String.format("S%d_N%d_L%d", env.getDepth(), env.getNumber(), env.countLabel());
        String endLabel = String.format("S%d_N%d_L%d", env.getDepth(), env.getNumber(), env.countLabel());

        output.add("  izj " + falseStartLabel);

        trueValue.toAssemble(output, env);
        if (DataType.getHigher(trueValueDataType, finalDataType) != trueValueDataType) {
            output.add(String.format("  cast %d, %d", trueValueDataType.code, finalDataType.code));
        }

        output.add("  jmp " + endLabel);

        output.add(falseStartLabel + ":");
        falseValue.toAssemble(output, env);
        if (DataType.getHigher(falseValueDataType, finalDataType) != falseValueDataType) {
            output.add(String.format("  cast %d, %d", falseValueDataType.code, finalDataType.code));
        }

        output.add(endLabel + ":");
    }
}

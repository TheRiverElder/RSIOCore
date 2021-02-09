package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class BinaryExpression extends AST {

    private final AST leftOperand;
    private final Operator operator;
    private final AST rightOperand;

    public BinaryExpression(int position, AST leftOperand, Operator operator, AST rightOperand) {
        super(position);
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder
                .write('(').write(leftOperand)
                .write(' ').write(operator.getLiteral()).write(' ')
                .write(rightOperand).write(')');
    }

    @Override
    public DataType getDataType(CompileEnvironment env) throws RSIOCompileException {
        DataType leftDataType = leftOperand.getDataType(env);
        DataType rightDataType = rightOperand.getDataType(env);
        return leftDataType.length >= rightDataType.length ? leftDataType : rightDataType;
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        DataType leftDataType = leftOperand.getDataType(env);
        DataType rightDataType = rightOperand.getDataType(env);
        DataType finalDataType = leftDataType.length >= rightDataType.length ? leftDataType : rightDataType;

        leftOperand.toAssemble(output, env);
        if (leftDataType.length < finalDataType.length || leftDataType.isFloat != finalDataType.isFloat) {
            output.add(String.format("  cast %d, %d", leftDataType.code, finalDataType.code));
        }

        rightOperand.toAssemble(output, env);
        if (rightDataType.length < finalDataType.length || rightDataType.isFloat != finalDataType.isFloat) {
            output.add(String.format("  cast %d, %d", rightDataType.code, finalDataType.code));
        }

        output.add(String.format("  %s %d", operator.getAsmHead(), finalDataType.code));
    }
}

package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.AssembleUtils;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.*;

public class BinaryExpression extends AST {

    public static final Set<Operator> COMPARISON_OPERATORS = new HashSet<>(Arrays.asList(
            Operator.EQUAL, Operator.NOT_EQUAL, Operator.GREATER, Operator.GREATER_OR_EQUAL, Operator.LESS, Operator.LESS_OR_EQUAL
    ));

    public static final Set<Operator> LOGIC_OPERATORS = new HashSet<>(Arrays.asList(
            Operator.AND, Operator.OR
    ));

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
        return (LOGIC_OPERATORS.contains(operator) || COMPARISON_OPERATORS.contains(operator)) ? DataType.BOOLEAN : DataType.getHigher(leftDataType, rightDataType);
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        DataType leftDataType = leftOperand.getDataType(env);
        DataType rightDataType = rightOperand.getDataType(env);

        if (LOGIC_OPERATORS.contains(operator)) {
            if (leftDataType != DataType.BOOLEAN)
                throw new RSIOCompileException("Left operand should be boolean type", leftOperand.getPosition());
            if (rightDataType != DataType.BOOLEAN)
                throw new RSIOCompileException("Right operand should be boolean type", rightOperand.getPosition());

            leftOperand.toAssemble(output, env);
            rightOperand.toAssemble(output, env);

            output.add(String.format("  %s", operator.getAsmHead()));
        } else {
            DataType finalDataType = DataType.getHigher(leftDataType, rightDataType);

            leftOperand.toAssemble(output, env);
            AssembleUtils.checkAndCast(output, leftDataType, finalDataType);

            rightOperand.toAssemble(output, env);
            AssembleUtils.checkAndCast(output, rightDataType, finalDataType);

            output.add(String.format("  %s.%d", operator.getAsmHead(), finalDataType.length));
        }

    }
}

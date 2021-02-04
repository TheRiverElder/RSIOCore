package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.Instructions;
import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.Scope;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.instruction.Calc;
import top.riverelder.rsio.core.instruction.Instruction;

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
    public void toSource(StringBuilder builder) {
        builder.append('(');
        leftOperand.toSource(builder);
        builder.append(' ').append(operator.getLiteral()).append(' ');
        rightOperand.toSource(builder);
        builder.append(')');
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        DataType leftDataType = leftOperand.getDataType(env);
        DataType rightDataType = rightOperand.getDataType(env);
        return leftDataType.length >= rightDataType.length ? leftDataType : rightDataType;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {
        leftOperand.toBytes(env);
        rightOperand.toBytes(env);
        byte head = 0;
        switch (operator) {
            case POWER: head = Instructions.HEAD_POW; break;

            case MULTIPLY: head = Instructions.HEAD_MUL; break;
            case DIVIDE: head = Instructions.HEAD_DIV; break;
            case MODULO: head = Instructions.HEAD_MOD; break;
            case PLUS: head = Instructions.HEAD_PLUS; break;
            case SUBTRACT: head = Instructions.HEAD_SUB; break;

            case EQUAL: head = Instructions.HEAD_EQ; break;
            case NOT_EQUAL: head = Instructions.HEAD_NE; break;
            case GREATER: head = Instructions.HEAD_GT; break;
            case LESS: head = Instructions.HEAD_LT; break;
            case GREATER_OR_EQUAL: head = Instructions.HEAD_GE; break;
            case LESS_OR_EQUAL: head = Instructions.HEAD_LE; break;

            case AND: head = Instructions.HEAD_AND; break;
            case OR: head = Instructions.HEAD_OR; break;
        }
        env.getBytesWriter().writeByte(head);
    }

    @Override
    public void toAssemble(List<Instruction> res, Scope scope) {
        leftOperand.toAssemble(res, scope);
        rightOperand.toAssemble(res, scope);
        res.add(new Calc(4, operator));
    }
}

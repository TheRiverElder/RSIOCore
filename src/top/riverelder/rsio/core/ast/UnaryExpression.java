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

public class UnaryExpression extends AST {

    private final Operator operator;
    private final AST operand;

    public UnaryExpression(int position, Operator operator, AST operand) {
        super(position);
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append(operator.getLiteral());
        operand.toSource(builder);
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        return operand.getDataType(env);
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {
        operand.toBytes(env);
        byte head = 0;
        switch (operator) {
            case NEGATIVE: head = Instructions.HEAD_NEG; break;
            case NOT: head = Instructions.NOT.head; break;
        }
        env.getBytesWriter().writeByte(head);
    }

    @Override
    public void toAssemble(List<Instruction> res, Scope scope) {
        operand.toAssemble(res, scope);
        res.add(new Calc(4, operator));
    }
}

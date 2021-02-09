package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

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
    public void toSource(BufferedStringBuilder builder) {
        builder.write(operator.getLiteral());
        operand.toSource(builder);
    }

    @Override
    public DataType getDataType(CompileEnvironment env) throws RSIOCompileException {
        return operand.getDataType(env);
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        operand.toAssemble(output, env);
        output.add(String.format("  %s %d", operator.getAsmHead(), operand.getDataType(env).code));
    }
}

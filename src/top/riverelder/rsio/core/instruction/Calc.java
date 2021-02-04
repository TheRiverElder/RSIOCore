package top.riverelder.rsio.core.instruction;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public class Calc extends Instruction {

    private final int dataSize;
    private final Operator operator;

    public Calc(int dataSize, Operator operator) {
        this.dataSize = dataSize;
        this.operator = operator;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {

    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append("  ").append(operator.getAsmHead()).append('_').append(dataSize).append(";\n");
    }
}

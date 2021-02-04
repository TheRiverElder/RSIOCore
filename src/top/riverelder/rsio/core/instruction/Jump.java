package top.riverelder.rsio.core.instruction;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public class Jump extends Instruction {

    private final boolean needEqualZero;

    public Jump(boolean needEqualZero) {
        this.needEqualZero = needEqualZero;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {

    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append("  ").append(needEqualZero ? "izj" : "jmp").append(";\n");
    }
}

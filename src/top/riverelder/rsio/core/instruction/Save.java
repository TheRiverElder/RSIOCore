package top.riverelder.rsio.core.instruction;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public class Save extends Instruction {

    private final int dataSize;

    public Save(int dataSize) {
        this.dataSize = dataSize;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {

    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append("  save_").append(dataSize).append(";\n");
    }
}

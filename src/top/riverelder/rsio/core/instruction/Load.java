package top.riverelder.rsio.core.instruction;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public class Load extends Instruction {

    private final int dataSize;

    public Load(int dataSize) {
        this.dataSize = dataSize;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {

    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append("  load_").append(dataSize).append(";\n");
    }
}

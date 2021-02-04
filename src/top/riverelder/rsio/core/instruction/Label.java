package top.riverelder.rsio.core.instruction;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public class Label extends Instruction {

    private final String name;

    public Label(String name) {
        this.name = name;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {

    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append(name).append(":\n");
    }
}

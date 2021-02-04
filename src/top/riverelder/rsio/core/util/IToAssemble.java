package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.compile.Scope;
import top.riverelder.rsio.core.instruction.Instruction;

import java.util.List;

public interface IToAssemble {
    void toAssemble(List<Instruction> res, Scope scope);
}

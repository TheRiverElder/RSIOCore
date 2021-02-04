package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public interface IToBytes {
    public void toBytes(CompileEnvironment env) throws RSIOCompileException;
}

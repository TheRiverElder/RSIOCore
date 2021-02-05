package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;

public interface IToBytes {
    public void toBytes(NestedCompileEnvironment env) throws RSIOCompileException;
}

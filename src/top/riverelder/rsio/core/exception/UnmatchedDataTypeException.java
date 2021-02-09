package top.riverelder.rsio.core.exception;

import top.riverelder.rsio.core.compile.DataType;

public class UnmatchedDataTypeException extends RSIOCompileException {
    public UnmatchedDataTypeException(DataType t1, DataType t2, int position) {
        super(String.format("Unmatched data type: %s and %s", t1.name, t2.name), position);
    }
}

package top.riverelder.rsio.core.compile;

import top.riverelder.rsio.core.ast.FunctionDefine;

public interface CompileEnvironment {

    boolean hasParent();

    CompileEnvironment getParent();

    Field createFunctionField(String name, FunctionInfo functionInfo, boolean isConstant);

    Field createField(String name, DataType type, boolean isConstant);

    Field getField(String name);

    void putDataType(DataType dataType);

    DataType getDataType(String name);

    int countLabel();
}

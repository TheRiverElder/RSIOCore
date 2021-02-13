package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.*;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionDefine extends AST {

    private final String name;
    private final List<String> parameterNames;
    private final List<String> parameterDataTypeNames;
    private final String resultDataTypeName;
    private final AST body;

    public FunctionDefine(int position, String name, List<String> parameterNames, List<String> parameterDataTypeNames, String resultDataTypeName, AST body) {
        super(position);
        this.name = name;
        this.parameterNames = parameterNames;
        this.parameterDataTypeNames = parameterDataTypeNames;
        this.resultDataTypeName = resultDataTypeName;
        this.body = body;
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        return DataType.FUNCTION;
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        List<DataType> parameterDataTypes = new ArrayList<>(parameterNames.size());
        FunctionCompileEnvironment bodyEnv = new FunctionCompileEnvironment(env);
        bodyEnv.createField("__FUNCTION_NAME__", DataType.STRING, true);
        for (int i = 0; i < parameterNames.size(); i++) {
            bodyEnv.createField(parameterNames.get(i), env.getDataType(parameterDataTypeNames.get(i)), false);
            DataType dataType = env.getDataType(parameterDataTypeNames.get(i));
            if (dataType == null)
                throw new RSIOCompileException("Undefined data type: " + parameterDataTypeNames.get(i), getPosition());
            parameterDataTypes.add(dataType);
        }

        DataType resultDataType;
        if (resultDataTypeName != null) {
            DataType bodyDataType = body.getDataType(bodyEnv);
            resultDataType = env.getDataType(resultDataTypeName);
            if (resultDataType == null)
                throw new RSIOCompileException("Undefined data type: " + resultDataTypeName, getPosition());
            if (bodyDataType.level > resultDataType.level)
                throw new RSIOCompileException("Unmatched data type: " + resultDataType.name + ", " + bodyDataType.name, getPosition());
        } else {
            resultDataType = DataType.VOID;
        }

        FunctionInfo info = new FunctionInfo(name, resultDataType, parameterDataTypes);
        env.createFunctionField(name, info, false);

        output.add("sect " + name);
        body.toAssemble(output, bodyEnv);
        output.add("end");
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write("function ").write(name).write('(');
        for (int i = 0; i < parameterNames.size(); i++) {
            builder.write(parameterNames.get(i)).write(": ").write(parameterDataTypeNames.get(i));
            if (i < parameterNames.size() - 1) {
                builder.write(", ");
            }
        }
        builder.write(")").write(resultDataTypeName == null ? "" : ": " + resultDataTypeName).write(body instanceof Scope ? " " : " = ").write(body);
    }
}

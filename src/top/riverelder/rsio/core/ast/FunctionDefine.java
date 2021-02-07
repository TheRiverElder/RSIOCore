package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class FunctionDefine extends AST {

    private final AST host;
    private final DataType resultDataType;
    private final List<DataType> parameterDataTypes;
    private final List<String> parameterNames;
    private final AST body;

    public FunctionDefine(int position, AST host, DataType resultDataType, List<DataType> parameterDataTypes, List<String> parameterNames, AST body) {
        super(position);
        this.host = host;
        this.resultDataType = resultDataType;
        this.parameterDataTypes = parameterDataTypes;
        this.parameterNames = parameterNames;
        this.body = body;
    }

    @Override
    public DataType getDataType(NestedCompileEnvironment env) {
        return resultDataType;
    }

    @Override
    public void toAssemble(List<String> output, NestedCompileEnvironment env) throws RSIOCompileException {

    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write(host).write('(');
        for (int i = 0; i < parameterNames.size(); i++) {
            builder.write(parameterNames.get(i)).write(": ").write(parameterDataTypes.get(i).name);
            if (i < parameterNames.size() - 1) {
                builder.write(", ");
            }
        }
        builder.write(") ").write(body);
    }
}

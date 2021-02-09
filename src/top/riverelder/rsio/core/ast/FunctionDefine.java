package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.FunctionCompileEnvironment;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

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
        NestedCompileEnvironment bodyEnv = new FunctionCompileEnvironment(env);
        bodyEnv.createField("__FUNCTION_NAME__", DataType.STRING, true);
        for (int i = 0; i < parameterNames.size(); i++) {
            bodyEnv.createField(parameterNames.get(i), env.getDataType(parameterDataTypeNames.get(i)), false);
        }
        body.toAssemble(output, bodyEnv);
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
        builder.write("): ").write(resultDataTypeName).write(body instanceof Scope ? "" : " = ").write(body);
    }
}

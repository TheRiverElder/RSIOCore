package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.FunctionInfo;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.exception.UnmatchedDataTypeException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.Iterator;
import java.util.List;

public class FunctionCall extends AST {

    private final AST host;
    private final List<AST> arguments;

    public FunctionCall(int position, AST host, List<AST> arguments) {
        super(position);
        this.host = host;
        this.arguments = arguments;
    }

    @Override
    public DataType getDataType(CompileEnvironment env) throws RSIOCompileException {
        DataType hostDataType = host.getDataType(env);
        if (hostDataType != DataType.FUNCTION || hostDataType.functionInfo == null) throw new UnmatchedDataTypeException(DataType.FUNCTION, hostDataType, getPosition());
        return hostDataType.functionInfo.resultDataType;
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        for (AST argument : arguments) {
            argument.toAssemble(output, env);
        }
        host.toAssemble(output, env);
        output.add("  call");
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write(host).write('(');
        Iterator<AST> itr = arguments.iterator();
        while (itr.hasNext()) {
            builder.write(itr.next());
            if (itr.hasNext()) {
                builder.write(", ");
            }
        }
        builder.write(')');
    }
}

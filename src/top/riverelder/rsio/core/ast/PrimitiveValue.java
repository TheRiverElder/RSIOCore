package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.Instructions;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.Scope;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.instruction.Instruction;
import top.riverelder.rsio.core.instruction.Load;
import top.riverelder.rsio.core.instruction.Push;
import top.riverelder.rsio.core.token.Token;

import java.util.List;

public class PrimitiveValue extends AST {

    private final Token token;

    public PrimitiveValue(Token token) {
        super(token.getPosition());
        this.token = token;
    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append(token.getContent());
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        switch (token.getType()) {
            case VARIABLE_NAME: return env.getField((String) token.getContent()).type;
            case INTEGER: return DataType.INTEGER;
            case DECIMAL: return DataType.DECIMAL;
            case STRING: return DataType.STRING;
            case BOOLEAN: return DataType.BOOLEAN;
            default: return null;
        }
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {
        switch (token.getType()) {
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
                env.getBytesWriter()
                        .writeByte(Instructions.HEAD_PUSH)
                        .writeInteger((Integer) token.getContent());
                break;
            case VARIABLE_NAME:
                env.getBytesWriter()
                        .writeByte(Instructions.HEAD_PUSH)
                        .writeInteger(env.getField((String) token.getContent()).position)
                        .writeByte(Instructions.HEAD_LOAD);
                break;
            case STRING: break;
            default: break;
        }
    }

    @Override
    public void toAssemble(List<Instruction> res, Scope scope) {
        switch (token.getType()) {
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
                res.add(new Push(4, (Integer) token.getContent()));
                break;
            case VARIABLE_NAME:
                res.add(new Push(4, scope.getField((String) token.getContent()).position));
                res.add(new Load(4));
                break;
            case STRING: break;
            default: break;
        }
    }
}

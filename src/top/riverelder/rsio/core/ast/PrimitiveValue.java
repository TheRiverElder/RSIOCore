package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.CompileEnvironment;
import top.riverelder.rsio.core.compile.Field;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.token.Token;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class PrimitiveValue extends AST {

    private final Token token;

    public PrimitiveValue(Token token) {
        super(token.getPosition());
        this.token = token;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write(token.getContent());
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        switch (token.getType()) {
            case FIELD_NAME: return env.getField((String) token.getContent()).type;
            case INTEGER: return DataType.INTEGER;
            case DECIMAL: return DataType.DECIMAL;
            case STRING: return DataType.STRING;
            case BOOLEAN: return DataType.BOOLEAN;
            default: return null;
        }
    }

    @Override
    public void toAssemble(List<String> output, CompileEnvironment env) throws RSIOCompileException {
        switch (token.getType()) {
            case INTEGER:output.add(String.format("  push %d, %d", DataType.INTEGER.length, (Integer) token.getContent())); break;
            case DECIMAL:output.add(String.format("  push %d, %f", DataType.DECIMAL.length, (Double) token.getContent())); break;
            case BOOLEAN: output.add(String.format("  push %d, %d", DataType.BOOLEAN.length, (Boolean) token.getContent() ? 1 : 0)); break;
            case FIELD_NAME:
                Field field = env.getField((String) token.getContent());
                if (field == null) throw new RSIOCompileException("Undefined variable: " + token.getContent(), token.getPosition());
                output.add(String.format("  push %d, %d", DataType.ADDRESS_LENGTH, field.position));
                output.add("  load " + field.type.length);
                break;
            case STRING: break;
            default: break;
        }
    }
}

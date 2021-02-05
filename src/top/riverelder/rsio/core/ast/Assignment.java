package top.riverelder.rsio.core.ast;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.Field;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class Assignment extends AST {

    private final AST host;
    private final String field;
    private final AST value;

    public Assignment(int position, @Nullable AST host, @NotNull String field,@NotNull AST value) {
        super(position);
        this.host = host;
        this.field = field;
        this.value = value;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        if (host != null) {
            builder.write(host).write('.');
        }
        builder.write(field).write(" = ").write(value);
    }

    @Override
    public DataType getDataType(NestedCompileEnvironment env) {
        return env.getField(field).type;
    }

    @Override
    public void toAssemble(List<String> output, NestedCompileEnvironment env) throws RSIOCompileException {
        Field field = env.getField(this.field);
        if (field == null) throw new RSIOCompileException("Undefined field: " + this.field, this.getPosition());
        if (field.isConstant) throw new RSIOCompileException("Cannot modify a constant field: " + this.field, this.getPosition());

        value.toAssemble(output, env);
        output.add("  push 4, " + field.position);
        output.add("  save");
    }
}

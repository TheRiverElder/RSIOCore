package top.riverelder.rsio.core.ast;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.Instructions;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.Field;
import top.riverelder.rsio.core.compile.Scope;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.instruction.Instruction;
import top.riverelder.rsio.core.instruction.Save;

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
    public void toSource(StringBuilder builder) {
        if (host != null) {
            host.toSource(builder);
            builder.append('.');
        }
        builder.append(field).append(" = ");
        value.toSource(builder);
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {
        Field f = env.getField(this.field);
        if (f == null) {
            f = env.createField(this.field, DataType.INTEGER);
        }
        DataType valueDataType = value.getDataType(env);
        if (f.type != valueDataType) throw new RSIOCompileException("Incompatible data type", this.getPosition());

        value.toBytes(env);
        env.getBytesWriter().writeByte(Instructions.HEAD_PUSH);
        env.getBytesWriter().writeInteger(f.position);
        env.getBytesWriter().writeByte(Instructions.HEAD_SAVE);
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        return env.getField(field).type;
    }

    @Override
    public void toAssemble(List<Instruction> res, Scope scope) {
        value.toAssemble(res, scope);
        res.add(new Save(4));
    }
}

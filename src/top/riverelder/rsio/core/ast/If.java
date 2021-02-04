package top.riverelder.rsio.core.ast;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.Instructions;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.Scope;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.instruction.Instruction;
import top.riverelder.rsio.core.instruction.Jump;
import top.riverelder.rsio.core.instruction.Push;
import top.riverelder.rsio.core.util.BytesWriter;

import java.util.List;

public class If extends AST {

    private final AST condition;
    private final AST ifTrueValue;
    private final AST ifFalseValue;

    public If(int position, @NotNull AST condition,@NotNull AST ifTrueValue, @Nullable AST ifFalseValue) {
        super(position);
        this.condition = condition;
        this.ifTrueValue = ifTrueValue;
        this.ifFalseValue = ifFalseValue;
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        return DataType.INTEGER;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {
        condition.toBytes(env);
        BytesWriter writer = env.getBytesWriter();
        writer.writeByte(Instructions.HEAD_PUSH);
        int index = writer.size();
        writer.writeInteger(0);
        writer.writeByte(Instructions.HEAD_IZJ);

        ifTrueValue.toBytes(env);
        if (ifFalseValue != null) {
            writer.writeByte(Instructions.HEAD_PUSH);
            int indexAfterTrueValue = writer.size();
            writer.writeInteger(0);
            writer.writeByte(Instructions.HEAD_JMP);
            writer.writeIntegerAt(index, writer.size());
            ifFalseValue.toBytes(env);
            writer.writeIntegerAt(indexAfterTrueValue, writer.size());
        } else {
            writer.writeIntegerAt(index, writer.size());
        }
    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append("if (");
        condition.toSource(builder);
        builder.append(") {");
        ifTrueValue.toSource(builder);
        builder.append("}");
        if (ifFalseValue != null) {
            builder.append(" else {");
            ifFalseValue.toSource(builder);
            builder.append("}");
        }
    }

    @Override
    public void toAssemble(List<Instruction> res, Scope scope) {
        condition.toAssemble(res, scope);
        Push toFalseValue = new Push(8, 0);
        Push toEnd = new Push(8, 0);
        res.add(toFalseValue);
        res.add(new Jump(true));

        if (ifFalseValue != null) {

        }
        ifTrueValue.toAssemble(res, scope);

    }
}

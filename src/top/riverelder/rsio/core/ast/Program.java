package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.CompileEnvironment;
import top.riverelder.rsio.core.Instructions;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.Scope;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.instruction.Instruction;
import top.riverelder.rsio.core.instruction.Label;
import top.riverelder.rsio.core.instruction.Pop;

import java.util.List;

public class Program extends AST {

    private final List<AST> statements;

    public Program(int position, List<AST> statements) {
        super(position);
        this.statements = statements;
    }

    @Override
    public DataType getDataType(CompileEnvironment env) {
        return statements.size() > 0 ? statements.get(statements.size() - 1).getDataType(env) : DataType.VOID;
    }

    @Override
    public void toBytes(CompileEnvironment env) throws RSIOCompileException {
        for (int i = 0; i < statements.size(); i++) {
            statements.get(i).toBytes(env);
            if (i < statements.size() - 1) {
                env.getBytesWriter().writeByte(Instructions.POP.head);
            }
        }
    }

    @Override
    public void toSource(StringBuilder builder) {
        for (AST statement : statements) {
            statement.toSource(builder);
            builder.append(";\n");
        }
    }

    @Override
    public void toAssemble(List<Instruction> res, Scope scope) {
        Scope innerScope = new Scope(scope);
        for (int i = 0; i < statements.size(); i++) {
            res.add(new Label(String.format("%s_%d_%d", "L", scope.getDepth(), i)));
            AST statement = statements.get(i);
            statement.toAssemble(res, innerScope);
            if (i < statements.size() - 1) {
                res.add(new Pop(4));
            }
        }
    }
}

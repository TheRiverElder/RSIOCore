package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.Iterator;
import java.util.List;

public class Scope extends AST {

    private final List<AST> statements;

    public Scope(int position, List<AST> statements) {
        super(position);
        this.statements = statements;
    }

    @Override
    public DataType getDataType(NestedCompileEnvironment env) {
        return statements.size() > 0 ? statements.get(statements.size() - 1).getDataType(env) : DataType.VOID;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write('{').line();
        builder.levelDown();
        for (AST statement : statements) {
            builder.write(statement).write(';').line();
        }
        builder.levelUp();
        builder.write('}');
    }

    @Override
    public void toAssemble(List<String> output, NestedCompileEnvironment env) throws RSIOCompileException {
        Iterator<AST> statementIterator = statements.iterator();
        while (statementIterator.hasNext()) {
            AST statement = statementIterator.next();
            statement.toAssemble(output, env);
            if (statementIterator.hasNext()) {
                output.add("  pop " + statement.getDataType(env).code);
            }
        }
    }
}

package top.riverelder.rsio.core.ast;

import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class FieldDefine extends AST {

    private final boolean constant;
    private final String name;
    private final String dataTypeName;
    private final AST initialValue;

    public FieldDefine(int position, boolean constant, String name, String dataTypeName, AST initialValue) {
        super(position);
        this.constant = constant;
        this.name = name;
        this.dataTypeName = dataTypeName;
        this.initialValue = initialValue;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write(constant ? "const" : "let").write(' ').write(name).write(": ").write(dataTypeName);
        if (initialValue != null) {
            builder.write(" = ").write(initialValue);
        }
    }

    @Override
    public DataType getDataType(NestedCompileEnvironment env) {
        return DataType.VOID;
    }

    @Override
    public void toAssemble(List<String> output, NestedCompileEnvironment env) throws RSIOCompileException {
        DataType dataType = env.getDataType(dataTypeName);
        if (dataType == null) throw new RSIOCompileException("Undefined data type: " + dataTypeName, this.getPosition());
        env.createField(name, dataType, constant);
        if (initialValue != null) {
            initialValue.toAssemble(output, env);
        }
    }
}


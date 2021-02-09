package top.riverelder.rsio.core.compile;

import top.riverelder.rsio.core.ast.FunctionDefine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CompileEnvironmentBase implements CompileEnvironment {

    protected final Map<String, Field> fields = new HashMap<>();
    protected final ArrayList<Field> fieldList = new ArrayList<>();
    protected int allocatedSize = 0;

    @Override
    public Field createFunctionField(String name, FunctionInfo functionInfo, boolean isConstant) {
        DataType fdt = DataType.FUNCTION;
        DataType type = new DataType(fdt.name, fdt.length, fdt.isFloat, fdt.level, functionInfo);

        if (this.fields.containsKey(name)) return null;
        int position = allocatedSize;
        allocatedSize += type.length;
        Field field = new Field(name, position, type, isConstant);
        fields.put(field.name, field);
        fieldList.add(field);
        return field;
    }

    @Override
    public Field createField(String name, DataType type, boolean isConstant) {
        if (this.fields.containsKey(name)) return null;
        int position = allocatedSize;
        allocatedSize += type.length;
        Field field = new Field(name, position, type, isConstant);
        fields.put(field.name, field);
        fieldList.add(field);
        return field;
    }

    @Override
    public Field getField(String name) {
        return fields.containsKey(name) ? fields.get(name) : (hasParent() ? getParent().getField(name) : null);
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

}

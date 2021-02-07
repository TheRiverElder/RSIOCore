package top.riverelder.rsio.core.compile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedCompileEnvironment {

    private final NestedCompileEnvironment parent;
    private int depth = 0;
    private int number = 0;
    private final Map<String, Field> fields;
    private final Map<String, DataType> dataTypes;
    private int allocatedSize = 0;
    private int labelCounter = 0;
    private int childEnvCounter = 0;
    private boolean doLabelCountBubble = true;

    public NestedCompileEnvironment(NestedCompileEnvironment parent) {
        this.parent = parent;
        if (parent != null) {
            this.depth = parent.depth + 1;
            this.number = parent.childEnvCounter++;
        }
        fields = new HashMap<>();
        dataTypes = new HashMap<>();
    }

    public NestedCompileEnvironment() {
        this(null);
    }

    public Field createField(String name, DataType type, boolean isConstant) {
        if (this.fields.containsKey(name)) return null;
        int position = allocatedSize;
        allocatedSize += type.length;
        Field field = new Field(name, position, type, isConstant);
        fields.put(field.name, field);
        return field;
    }

    public NestedCompileEnvironment getParent() {
        return parent;
    }

    public Field getField(String name) {
        return fields.containsKey(name) ? fields.get(name) : (parent == null ? null : parent.getField(name));
    }

    public int getAllocatedSize() {
        return allocatedSize;
    }

    public int countLabel() {
        return labelCounter = (doLabelCountBubble && parent != null) ? parent.countLabel() : labelCounter + 1;
    }

    public int getNumber() {
        return number;
    }

    public void setDoLabelCountBubble(boolean doLabelCountBubble) {
        this.doLabelCountBubble = doLabelCountBubble;
    }

    public int getDepth() {
        return depth;
    }

    public Collection<Field> getFieldList() {
        return fields.values();
    }



    public void putDataType(DataType dataType) {
        dataTypes.put(dataType.name, dataType);
    }

    public DataType getDataType(String name) {
        return dataTypes.get(name);
    }


}

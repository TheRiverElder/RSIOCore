package top.riverelder.rsio.core;

import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.Field;
import top.riverelder.rsio.core.util.BytesWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompileEnvironment {

    public static final int REG_AX = 0;
    public static final int REG_BX = 1;
    public static final int REG_CX = 2;
    public static final int REG_DX = 3;

    public static final int REG_HOST = 4;

    private final Map<String, Field> fields;
    private int allocatedSize;
    private final BytesWriter bytesWriter;

    public CompileEnvironment() {
        fields = new HashMap<>();
        allocatedSize = 0;
        bytesWriter = new BytesWriter();
    }

    public Field createField(String name, DataType type) {
        if (this.fields.containsKey(name)) return null;
        int position = allocatedSize;
        allocatedSize += type.length;
        Field field = new Field(name, position, type);
        fields.put(field.name, field);
        return field;
    }

    public Field getField(String name) {
        return fields.get(name);
    }


    public BytesWriter getBytesWriter() {
        return bytesWriter;
    }
}

package top.riverelder.rsio.core.compile;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private final Scope parent;
    private final int depth;
    private final Map<String, Field> fields;

    public Scope(Scope parent) {
        this.fields = new HashMap<>();
        this.depth = parent == null ? 0 : parent.depth + 1;
        this.parent = parent;
    }

    public Scope() {
        this(null);
    }

    public int getDepth() {
        return depth;
    }

    public Field getField(String name) {
        return fields.containsKey(name) ? fields.get(name) : (parent == null ? null : parent.getField(name));
    }

    public Field setField(Field field) {
        return fields.put(field.name, field);
    }
}

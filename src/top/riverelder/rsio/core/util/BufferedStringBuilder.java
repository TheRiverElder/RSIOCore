package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.ast.AST;

public class BufferedStringBuilder {

    private final StringBuilder builder = new StringBuilder();
    private int level = 0;
    private boolean isNewLine = true;

    public StringBuilder getBuilder() {
        return builder;
    }

    public int getLevel() {
        return level;
    }

    public int levelDown() {
        return ++level;
    }

    public int levelUp() {
        return --level;
    }

    public BufferedStringBuilder write(AST ast) {
        checkIndent();
        ast.toSource(this);
        return this;
    }


    public BufferedStringBuilder write(Object d) {
        checkIndent();
        builder.append(d);
        return this;
    }

    public BufferedStringBuilder write(char d) {
        checkIndent();
        builder.append(d);
        return this;
    }

    public BufferedStringBuilder write(String d) {
        checkIndent();
        builder.append(d);
        return this;
    }

    public BufferedStringBuilder write(int d) {
        checkIndent();
        builder.append(d);
        return this;
    }

    public BufferedStringBuilder line() {
        builder.append('\n');
        isNewLine = true;
        return this;
    }

    private void checkIndent() {
        if (isNewLine) {
            indent();
            isNewLine = false;
        }
    }

    public BufferedStringBuilder indent() {
        for (int i = 0; i < level; i++) {
            builder.append("    ");
        }
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}

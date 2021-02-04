package top.riverelder.rsio.core;

public enum Operator {

//    RIGHT_PAREN(")", 8),

    NEGATIVE("-", "neg", 7, true),
    POSITIVE("+", "pos", 7, true),

    POWER("^", "pow", 6),

    MULTIPLY("*", "mul", 5),
    DIVIDE("/", "div", 5),
    MODULO("%", "mod", 5),

    PLUS("+", "plus", 4),
    SUBTRACT("-", "sub", 4),

    EQUAL("==", "eq", 3),
    NOT_EQUAL("!=", "ne", 3),
    GREATER(">", "gt", 3),
    LESS("<", "lt", 3),
    GREATER_OR_EQUAL(">=", "ge", 3),
    LESS_OR_EQUAL("<=", "le", 3),

    NOT("!", "not", 2, true),

    AND("&&", "and", 1),
    OR("||", "or", 1),

//    LEFT_PAREN("(", 0),
    ;

    public static Operator of(String literal, int level) {
        for (Operator operator: Operator.values()) {
            if (operator.literal.equals(literal) && operator.level == level) return operator;
        }
        return null;
    }


    private final String literal;
    private final String asmHead;
    private final int level;
    private final boolean isUnaryOperator;

    Operator(String literal, String asmHead, int level) {
        this.literal = literal;
        this.asmHead = asmHead;
        this.level = level;
        this.isUnaryOperator = false;
    }

    Operator(String literal, String asmHead, int level, boolean isUnaryOperator) {
        this.literal = literal;
        this.asmHead = asmHead;
        this.level = level;
        this.isUnaryOperator = isUnaryOperator;
    }

    public String getLiteral() {
        return literal;
    }

    public String getAsmHead() {
        return asmHead;
    }

    public int getLevel() {
        return level;
    }

    public boolean isUnaryOperator() {
        return isUnaryOperator;
    }
}

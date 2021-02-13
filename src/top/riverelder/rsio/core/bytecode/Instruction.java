package top.riverelder.rsio.core.bytecode;

public enum Instruction {

    NOP(0),
    CAST(1),
    LOAD(2),
    SAVE(3),
    ALLOC(4),
    PUSH(5),
    POP(6),
    NEG(7),
    PLUS(8),
    SUB(9),
    MUL(10),
    DIV(11),
    MOD(12),
    POW(13),
    NEG_F(14),
    PLUS_F(15),
    SUB_F(16),
    MUL_F(17),
    DIV_F(18),
    MOD_F(19),
    POW_F(20),
    NOT(21),
    AND(22),
    OR(23),
    EQ(24),
    NE(25),
    GT(26),
    LT(27),
    GE(28),
    LE(29),
    EQ_F(30),
    NE_F(31),
    GT_F(32),
    LT_F(33),
    GE_F(34),
    LE_F(35),
    JMP(36),
    IZJ(37),
    EXIT(38),
    CALL(39),
    RETURN(40),
    WRITE(41),
    FLUSH(42),
    READ(43),
            ;

    public static final int HEAD_NOP = 0;
    public static final int HEAD_CAST = 1;
    public static final int HEAD_LOAD = 2;
    public static final int HEAD_SAVE = 3;
    public static final int HEAD_ALLOC = 4;
    public static final int HEAD_PUSH = 5;
    public static final int HEAD_POP = 6;
    public static final int HEAD_NEG = 7;
    public static final int HEAD_PLUS = 8;
    public static final int HEAD_SUB = 9;
    public static final int HEAD_MUL = 10;
    public static final int HEAD_DIV = 11;
    public static final int HEAD_MOD = 12;
    public static final int HEAD_POW = 13;
    public static final int HEAD_NEG_F = 14;
    public static final int HEAD_PLUS_F = 15;
    public static final int HEAD_SUB_F = 16;
    public static final int HEAD_MUL_F = 17;
    public static final int HEAD_DIV_F = 18;
    public static final int HEAD_MOD_F = 19;
    public static final int HEAD_POW_F = 20;
    public static final int HEAD_NOT = 21;
    public static final int HEAD_AND = 22;
    public static final int HEAD_OR = 23;
    public static final int HEAD_EQ = 24;
    public static final int HEAD_NE = 25;
    public static final int HEAD_GT = 26;
    public static final int HEAD_LT = 27;
    public static final int HEAD_GE = 28;
    public static final int HEAD_LE = 29;
    public static final int HEAD_EQ_F = 30;
    public static final int HEAD_NE_F = 31;
    public static final int HEAD_GT_F = 32;
    public static final int HEAD_LT_F = 33;
    public static final int HEAD_GE_F = 34;
    public static final int HEAD_LE_F = 35;
    public static final int HEAD_JMP = 36;
    public static final int HEAD_IZJ = 37;
    public static final int HEAD_EXIT = 38;
    public static final int HEAD_CALL = 39;
    public static final int HEAD_RETURN = 40;
    public static final int HEAD_WRITE = 41;
    public static final int HEAD_FLUSH = 42;
    public static final int HEAD_READ = 43;

    public final byte head;

    Instruction(int head) {
        this.head = (byte) head;
    }
}

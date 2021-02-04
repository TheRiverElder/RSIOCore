package top.riverelder.rsio.core;

public enum Instructions {

    NOP(0),
    LOAD(1),
    SAVE(2),
    PUSH(16),
    POP(17),
    NEG(32),
    PLUS(33),
    SUB(34),
    MUL(35),
    DIV(36),
    MOD(37),
    POW(38),
    NOT(48),
    AND(49),
    OR(50),
    EQ(64),
    NE(65),
    GT(66),
    LT(67),
    GE(68),
    LE(69),
    JMP(80),
    IZJ(81),
    EXIT(82),
    WRITE(96),
    FLUSH(97),
    READ(98),
    ;

    public static final byte HEAD_NOP = 0;
    public static final byte HEAD_LOAD = 1;
    public static final byte HEAD_SAVE = 2;
    public static final byte HEAD_PUSH = 16;
    public static final byte HEAD_POP = 17;
    public static final byte HEAD_NEG = 32;
    public static final byte HEAD_PLUS = 33;
    public static final byte HEAD_SUB = 34;
    public static final byte HEAD_MUL = 35;
    public static final byte HEAD_DIV = 36;
    public static final byte HEAD_MOD = 37;
    public static final byte HEAD_POW = 38;
    public static final byte HEAD_NOT = 48;
    public static final byte HEAD_AND = 49;
    public static final byte HEAD_OR = 50;
    public static final byte HEAD_EQ = 64;
    public static final byte HEAD_NE = 65;
    public static final byte HEAD_GT = 66;
    public static final byte HEAD_LT = 67;
    public static final byte HEAD_GE = 68;
    public static final byte HEAD_LE = 69;
    public static final byte HEAD_JMP = 80;
    public static final byte HEAD_IZJ = 81;
    public static final byte HEAD_EXIT = 82;
    public static final byte HEAD_WRITE = 96;
    public static final byte HEAD_FLUSH = 97;
    public static final byte HEAD_READ = 98;


    public final byte head;

    Instructions(byte head) {
        this.head = head;
    }

    Instructions(int head) {
        this((byte) head);
    }
}

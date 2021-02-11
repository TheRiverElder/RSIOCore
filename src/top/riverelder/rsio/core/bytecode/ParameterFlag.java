package top.riverelder.rsio.core.bytecode;

public enum ParameterFlag {

    NONE(0x0000, ' '),

    B8(0x0001, 'B'),
    B16(0x0010, 'S'),
    B32(0x0011, 'I'),
    B64(0x0100, 'L'),

    INSTANT(0x1000, '#'),
    ADDRESS(0x1000, '@'),
    ;

    public final int flag;
    public final char code;

    ParameterFlag(int flag, char code) {
        this.flag = flag;
        this.code = code;
    }
}

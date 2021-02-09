package top.riverelder.rsio.core.token;

import top.riverelder.rsio.core.util.BufferedStringBuilder;

public class IdToken extends Token {

    private final String id;
    private final boolean fieldNameFlag;

    public IdToken(String id, boolean fieldNameFlag) {
        this.id = id;
        this.fieldNameFlag = fieldNameFlag;
    }

    @Override
    public Object getContent() {
        return id;
    }

    @Override
    public TokenType getType() {
        return fieldNameFlag ? TokenType.FIELD_NAME : TokenType.OPERATOR;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write(id);
    }
}

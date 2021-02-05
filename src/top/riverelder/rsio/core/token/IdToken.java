package top.riverelder.rsio.core.token;

import top.riverelder.rsio.core.util.BufferedStringBuilder;

import java.util.List;

public class IdToken extends Token {

    private final String id;
    private final boolean isVariableName;

    public IdToken(String id, boolean isVariableName) {
        this.id = id;
        this.isVariableName = isVariableName;
    }

    @Override
    public Object getContent() {
        return id;
    }

    @Override
    public TokenType getType() {
        return isVariableName ? TokenType.VARIABLE_NAME : TokenType.OPERATOR;
    }

    @Override
    public void toSource(BufferedStringBuilder builder) {
        builder.write(id);
    }
}

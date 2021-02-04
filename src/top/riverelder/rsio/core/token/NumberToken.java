package top.riverelder.rsio.core.token;

public class NumberToken extends Token {

    private Number value;
    private boolean isInteger;

    public NumberToken(int value) {
        this.value = value;
        this.isInteger = true;
    }

    public NumberToken(double value) {
        this.value = value;
        this.isInteger = false;
    }

    @Override
    public TokenType getType() {
        return isInteger ? TokenType.INTEGER : TokenType.DECIMAL;
    }

    @Override
    public Object getContent() {
        return value;
    }

    @Override
    public void toSource(StringBuilder builder) {
        builder.append(value);
    }
}

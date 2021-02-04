package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.token.Token;
import top.riverelder.rsio.core.token.TokenType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TokenReader {

    private List<Token> tokens;
    private int cursor;

    public TokenReader(List<Token> tokens) {
        this.tokens = tokens;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public boolean hasMore() {
        return cursor < tokens.size();
    }

    public Token peek() {
        return tokens.get(cursor);
    }

    public Token read() {
        return tokens.get(cursor++);
    }

    public Token read(TokenType expectingType) {
        return hasMore() && peek().getType() == expectingType ? read() : null;
    }

    public boolean tryRead(String operator) {
        if (!hasMore() || peek().getType() != TokenType.OPERATOR || !Objects.equals(operator, peek().getContent())) return false;
        read();
        return true;
    }

    public Token read(String operator) {
        if (!hasMore() || peek().getType() != TokenType.OPERATOR || !Objects.equals(operator, peek().getContent())) return null;
        return read();
    }

    public Token read(String ...operators) {
        if (!hasMore() && peek().getType() != TokenType.OPERATOR) return null;
        Token t = peek();
        if (Arrays.stream(operators).anyMatch(id -> Objects.equals(id, t.getContent()))) return read();
        return null;
    }
}

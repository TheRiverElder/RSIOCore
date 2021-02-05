package top.riverelder.rsio.core;

import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.token.IdToken;
import top.riverelder.rsio.core.token.NumberToken;
import top.riverelder.rsio.core.token.Token;
import top.riverelder.rsio.core.util.StaticStringReader;

import java.util.*;
import java.util.function.Supplier;

public class RSIOTokenizer {

    public static boolean isVarNameHead(char ch) {
        return ch == '_' || ch == '$' || Character.isLetter(ch);
    }

    public static boolean isVarNameBody(char ch) {
        return ch == '_' || ch == '$' || Character.isLetterOrDigit(ch);
    }

    public static String[] OPERATORS = new String[] {
            "**", "+", "-", "*", "/", "%", // 四则运算
            "(", ")", "[", "]", "{", "}", // 括号
            ">=", "<=", "==", "!=", ">", "<", // 值比较
            "||", "&&", "!", // 逻辑运算
            "|", "&", "^", // 位运算
            "~", "#", "@", // 快捷操作
            ";", "=", ".", "=>",
            "?", ":",
            "if", "else", "while",
    };

    private final StaticStringReader reader;

    public RSIOTokenizer(StaticStringReader reader) {
        this.reader = reader;
    }

    public List<Token> tokenize() throws RSIOCompileException {
        List<Token> tokens = new ArrayList<>();

        List<Supplier<Token>> parsers = Arrays.asList(this::number, this::operator, this::varName);

        while (reader.hasMore()) {
            // 跳过空白与注释
            reader.skipWhitespace();
            while (skipComments()) {
                reader.skipWhitespace();
            }
            if (!reader.hasMore()) break;
            // 记录该token的起始位置
            int start = reader.getCursor();
            Token token = null;
            for (Supplier<Token> parser : parsers) {
                token = parser.get();
                if (token != null) break;
                reader.setCursor(start);
            }
            // 如果找不到则说明出错
            if (token == null) throw new RSIOCompileException("Unrecognized token", start);
            token.setPosition(start);
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * 跳过注释，如果检测到注释则返回真
     * @return 是否检测到注释
     */
    private boolean skipComments() {
        if (reader.read("//")) {
            reader.readUntil(c -> c == '\n');
            if (reader.hasMore()) {
                reader.read();
            }
            return true;
        }
        return false;
    }

    private IdToken varName() {
        if (!isVarNameHead(reader.peek())) return null;
        char varNameHead = reader.read();
        String varNameBody = reader.readFollowing(RSIOTokenizer::isVarNameBody);
        String varName = varNameHead + (varNameBody == null ? "" : varNameBody);
        return new IdToken(varName, true);
    }

    private IdToken operator() {
        String operator = reader.read(OPERATORS);
        return operator == null ? null : new IdToken(operator, false);
    }

    private NumberToken number() {
        if (!Character.isDigit(reader.peek())) return null;
        String digitPartString = reader.readFollowing(Character::isDigit);

        if (!reader.hasMore() || !reader.read('.')) {
            int digitValue = Integer.parseInt(digitPartString);
            return new NumberToken(digitValue);
        }
        if (!reader.hasMore()) return null;

        String fractionPartString = reader.readFollowing(Character::isDigit);
        if (fractionPartString == null) return null;
        double decimalValue = Double.parseDouble(digitPartString + "." + fractionPartString);

        return new NumberToken(decimalValue);
    }
}

package top.riverelder.rsio.core.compile;

import top.riverelder.rsio.core.Operator;
import top.riverelder.rsio.core.ast.*;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.token.Token;
import top.riverelder.rsio.core.token.TokenType;
import top.riverelder.rsio.core.util.TokenReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RSIOParser {

    private final TokenReader reader;

    public RSIOParser(TokenReader reader) {
        this.reader = reader;
    }

    public AST parse() throws RSIOCompileException {
        AST program = parseStatements();
        if (reader.hasMore()) throw new RSIOCompileException("Unexpected token", reader.peek().getPosition());
        return program;
    }

    private AST parseStatements() throws RSIOCompileException {
        int start = reader.getCursor();
        List<AST> statements = new ArrayList<>();

        AST ast;
        while (reader.tryRead(";"));
        while ((ast = parseStatement()) != null) {
            statements.add(ast);
            while (reader.tryRead(";"));
        }
        return new Scope(start, statements);
    }

    private AST parseValuable() throws RSIOCompileException {
        AST ast = parseScope();
        if (ast == null) ast = parseAssignment();
        if (ast == null) ast = parseSimpleExpression();
        if (ast == null) ast = parseBasicOperand();

        int start = reader.getCursor();
        if (ast != null && reader.tryRead("?")) {
            AST trueValue = parseValuable();
            if (trueValue != null && reader.tryRead(":")) {
                AST falseValue = parseValuable();
                if (falseValue != null) return new TrinaryExpression(ast.getPosition(), ast, trueValue, falseValue);
            }
        }
        reader.setCursor(start);
        return ast;
    }

    private AST parseStatement() throws RSIOCompileException {
        int start = reader.getCursor();
        AST ast;
        if ((ast = parseIf()) != null) return ast;
        if ((ast = parseWhile()) != null) return ast;
        if ((ast = parseScope()) != null) return ast;
        if ((ast = parseValuable()) != null && reader.tryRead(";")) return ast;
        reader.setCursor(start);
        return null;
    }

    private AST parseAssignment() throws RSIOCompileException {
        int start = reader.getCursor();
        Token fieldToken = reader.read(TokenType.VARIABLE_NAME);
        if (fieldToken == null) return null;

        String field = (String) fieldToken.getContent();
        if (!reader.tryRead("=")) {
            reader.setCursor(start);
            return null;
        }

        AST value = parseValuable();
        if (value == null) {
            reader.setCursor(start);
            return null;
        }
        return new Assignment(fieldToken.getPosition(), null, field, value);
    }

    private AST parseSimpleExpression() throws RSIOCompileException {
        Stack<BufferedOperator> operatorStack = new Stack<>();
        Stack<AST> resultStack = new Stack<>();

        AST operand;
        BufferedOperator bufferedOperator;
        boolean nextIsUnary = true;

        AST lastValidResult = null;
        int start = reader.getCursor();

        while (reader.hasMore()) {
            if ((operand = parseOperand()) != null) {
                resultStack.push(operand);
                nextIsUnary = false;
                if (resultStack.size() == 1 && operatorStack.isEmpty()) {
                    lastValidResult = operand;
                    start = reader.getCursor();
                }
            } else if ((bufferedOperator = parseOperator(nextIsUnary)) != null) {
                nextIsUnary = true;
                while (!operatorStack.isEmpty()
                        && bufferedOperator.operator.getLevel() <= operatorStack.peek().operator.getLevel()
                        && !bufferedOperator.operator.isUnaryOperator()) {
                    int s = popOperator(operatorStack, resultStack);
                    if (s == -2) {
                        reader.setCursor(start);
                        return lastValidResult;
                    } else if (s >= 0) {
                        start = s;
                        lastValidResult = resultStack.peek();
                    }
                }
                operatorStack.push(bufferedOperator);
            } else break;
        }

        while (!operatorStack.isEmpty()) {
            int s = popOperator(operatorStack, resultStack);
            if (s == -2) {
                reader.setCursor(start);
                return lastValidResult;
            } else if (s >= 0) {
                start = s;
                lastValidResult = resultStack.peek();
            }
        }

        reader.setCursor(start);
        return lastValidResult;

    }

    /**
     * 运算符出栈
     * @param operatorStack 运算符栈
     * @param resultStack 结果栈
     * @return 返回-1代表最近的可用位置不变，返回-2代表出现栈异常，直接结束解析并返回最近的可行值，返回0或正数代表更新最新的可行值
     */
    private int popOperator(Stack<BufferedOperator> operatorStack, Stack<AST> resultStack) {
        BufferedOperator previousOperator = operatorStack.pop();

        boolean previousOperatorIsUnary = previousOperator.operator.isUnaryOperator();

        if (resultStack.size() < (previousOperatorIsUnary ? 1 : 2)) return -2;

        AST rightOperand = resultStack.pop();
        AST leftOperand = previousOperatorIsUnary ? null : resultStack.pop();
        resultStack.push(
                previousOperatorIsUnary
                        ? new UnaryExpression(rightOperand.getPosition(), previousOperator.operator, rightOperand)
                        : new BinaryExpression(leftOperand.getPosition(), leftOperand, previousOperator.operator, rightOperand));

        if (resultStack.size() == 1 && operatorStack.isEmpty()) return reader.getCursor();
        return -1;
    }

    /**
     * basic_operand = INTEGER | DECIMAL | STRING | ID
     * @return result
     */
    private AST parseBasicOperand() {
        Token token = reader.read(TokenType.VARIABLE_NAME);
        if (token == null) {
            token = reader.read(TokenType.INTEGER);
        }
        if (token == null) {
            token = reader.read(TokenType.DECIMAL);
        }
        return token == null ? null : new PrimitiveValue(token);
    }

    private AST parseOperand() throws RSIOCompileException {
        AST operand = parseClosedExpression();
        if (operand != null) return operand;
        if ((operand = parseBasicOperand()) != null) return operand;
        return null;
    }

    private BufferedOperator parseOperator(boolean isUnary) {
        Token t = reader.peek();
        if (!reader.hasMore() || t.getType() != TokenType.OPERATOR) return null;
        for (Operator operator: Operator.values()) {
            if (operator.getLiteral().equals(t.getContent()) && operator.isUnaryOperator() == isUnary) {
                return new BufferedOperator(operator, reader.read());
            }
        }
        return null;
    }

    private static class BufferedOperator {
        final Operator operator;
        final Token token;

        public BufferedOperator(Operator operator, Token token) {
            this.operator = operator;
            this.token = token;
        }
    }

    private AST parseClosedExpression() throws RSIOCompileException {
        int start = reader.getCursor();
        AST ast;
        if (reader.tryRead("(") && (ast = parseValuable()) != null && reader.tryRead(")")) {
            return ast;
        }
        reader.setCursor(start);
        return null;
    }

    private AST parseIf() throws RSIOCompileException {
        int start = reader.getCursor();
        Token ifToken = reader.read("if");

        AST condition;
        if (ifToken != null && (condition = parseClosedExpression()) != null) {
            AST ifTrueValue = parseScope();
            if (ifTrueValue == null) {
                ifTrueValue = parseStatement();
            }
            if (ifTrueValue != null) {
                if (reader.tryRead("else")) {
                    AST ifFalseValue = parseScope();
                    ifFalseValue = ifFalseValue == null ? parseStatement() : ifFalseValue;
                    if (ifFalseValue != null) {
                        return new If(ifToken.getPosition(), condition, ifTrueValue, ifFalseValue);
                    }
                } else return new If(ifToken.getPosition(), condition, ifTrueValue, null);
            }
        }
        reader.setCursor(start);
        return null;
    }

    private AST parseWhile() throws RSIOCompileException {
        int start = reader.getCursor();
        Token whileToken = reader.read("while");

        AST condition;
        if (whileToken != null && (condition = parseClosedExpression()) != null) {
            AST body = parseScope();
            body = body == null ? parseStatement() : body;
            if (body != null) return new While(whileToken.getPosition(), condition, body);
        }
        reader.setCursor(start);
        return null;
    }

    private AST parseScope() throws RSIOCompileException {
        int start = reader.getCursor();

        if (reader.tryRead("{")) {
            AST content = parseStatements();
            if (content != null && reader.tryRead("}")) {
                return content;
            }
        }

        reader.setCursor(start);
        return null;
    }
}
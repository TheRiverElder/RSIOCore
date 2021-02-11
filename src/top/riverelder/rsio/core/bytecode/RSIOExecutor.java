package top.riverelder.rsio.core.bytecode;

import top.riverelder.rsio.core.util.ByteArrays;
import top.riverelder.rsio.core.util.Convert;

import static top.riverelder.rsio.core.bytecode.Instructions.*;

public class RSIOExecutor {

    public static boolean isInstant(byte flag) {
        return (flag & 0b00001000) != 0;
    }

    public static int getLength(byte flag) {
        return flag & 0b00000111;
    }

    private int programCounter;
    private int stackPointer;
    private byte[] stack;
    private byte[] memory;
    private byte[] bytes;

    public long readProgram(int length) {
        long value = ByteArrays.read(bytes, programCounter, length);
        stackPointer += length;
        return value;
    }

    public long pop(int length) {
        long value = ByteArrays.read(stack, stackPointer - length, length);
        stackPointer -= length;
        return value;
    }

    public long peek(int length) {
        long value = ByteArrays.read(stack, stackPointer - length, length);
        return value;
    }

    public void push(int length, long value) {
        ByteArrays.write(stack, stackPointer, length, value);
        stackPointer += length;
    }

    public void initialize(byte[] bytes, int stackSize, int memorySize) {
        programCounter = 0;
        stackPointer = 0;
        stack = new byte[stackSize];
        memory = new byte[memorySize];
        this.bytes = bytes;
    }

    public void execute() {
        while (programCounter >= 0 && programCounter < bytes.length) {
            byte head = bytes[programCounter++];
            byte flags = bytes[programCounter++];

            switch (head) {
                case HEAD_NOP: break;
                case HEAD_CAST: cast(flags & 0xF0, flags & 0x0F); break;
                case HEAD_LOAD: load(flags); break;
                case HEAD_SAVE: save(flags); break;

                case HEAD_PUSH: push(flags, readProgram(flags)); break;
                case HEAD_POP: pop(flags); break;

                case HEAD_PLUS:
                case HEAD_SUB:
                case HEAD_MUL:
                case HEAD_DIV:
                case HEAD_MOD:
                case HEAD_POW: mathematics(head, flags); break;

                case HEAD_EQ:
                case HEAD_NE:
                case HEAD_GT:
                case HEAD_LT:
                case HEAD_GE:
                case HEAD_LE: compare(head, flags); break;

                case HEAD_AND:
                case HEAD_OR:
                case HEAD_NOT: logic(head, flags); break;

                case HEAD_JMP: programCounter = (int) readProgram(4); break;
                case HEAD_IZJ:
                    if (pop(getLength(flags)) == 0) {
                        programCounter = (int) readProgram(4);
                    } break;
                case HEAD_EXIT: programCounter = bytes.length; break;
            }
        }
    }

    public void load(int length) {
        int memoryLocation = (int) readProgram(4);
        long value = ByteArrays.read(memory, memoryLocation, length);
        push(length, value);
    }

    public void save(int length) {
        int memoryLocation = (int) readProgram(4);
        long value = peek(length);
        ByteArrays.write(memory, memoryLocation, length, value);
    }

    public void cast(int originType, int targetType) {
        // TODO
    }

    public void mathematics(byte head, byte flags) {
        int operandLength = getLength(flags);

        long leftOperand = pop(operandLength);

        if (head == Instructions.HEAD_NEG) {
            push(operandLength, Convert.convert(-pop(operandLength), operandLength));
            return;
        }

        long rightOperand = isInstant(flags) ? readProgram(operandLength) : pop(operandLength);

        long result = 0;
        switch (head) {
            case Instructions.HEAD_PLUS: result = leftOperand + rightOperand; break;
            case HEAD_SUB: result = leftOperand - rightOperand; break;
            case Instructions.HEAD_MUL: result = leftOperand * rightOperand; break;
            case Instructions.HEAD_DIV: result = leftOperand / rightOperand; break;
            case Instructions.HEAD_MOD: result = leftOperand % rightOperand; break;
            case Instructions.HEAD_POW: result = (long) Math.pow(leftOperand, rightOperand); break;
        }

        push(operandLength, Convert.convert(result, operandLength));
    }

    public void compare(byte head, byte flags) {
        int operandLength = getLength(flags);

        long leftOperand = pop(operandLength);
        long rightOperand = isInstant(flags) ? readProgram(operandLength) : pop(operandLength);

        boolean result = false;
        switch (head) {
            case Instructions.HEAD_EQ: result = leftOperand == rightOperand; break;
            case Instructions.HEAD_NE: result = leftOperand != rightOperand; break;
            case Instructions.HEAD_GT: result = leftOperand > rightOperand; break;
            case Instructions.HEAD_LT: result = leftOperand < rightOperand; break;
            case Instructions.HEAD_GE: result = leftOperand >= rightOperand; break;
            case Instructions.HEAD_LE:  result = leftOperand <= rightOperand; break;
        }

        push(1, result ? 1 : 0);
    }

    public void logic(byte head, byte flags) {
        int operandLength = getLength(flags);

        long leftOperand = pop(operandLength);

        if (head == Instructions.HEAD_NOT) {
            push(1, pop(1) == 0 ? 1 : 0);
            return;
        }

        long rightOperand = isInstant(flags) ? readProgram(operandLength) : pop(operandLength);

        boolean result = false;
        switch (head) {
            case Instructions.HEAD_AND: result = leftOperand == rightOperand; break;
            case Instructions.HEAD_OR: result = leftOperand != rightOperand; break;
            case Instructions.HEAD_GT: result = leftOperand > rightOperand; break;
        }

        push(1, result ? 1 : 0);
    }
}

package top.riverelder.rsio.core.bytecode;

import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.util.ByteArrays;
import top.riverelder.rsio.core.util.Convert;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import static top.riverelder.rsio.core.bytecode.Instruction.*;

public class RSIOExecutor {

    public static boolean isInstant(byte flag) {
        return (flag & 0b00001000) != 0;
    }

    public static int getLength(byte flag) {
        return flag & 0b00000111;
    }

    private byte[] program;
    private int programCounter;
    private byte[] stack;
    private int stackPointer;
    private byte[] memory;
    private int memoryOffset;
    private int memoryAllocStart;
    private Stack<Integer> callStack;
    private Map<Integer, Consumer<RSIOExecutor>> nativeFunctions;

    public byte[] getMemory() {
        return memory;
    }

    public long readProgram(int length) {
        long value = ByteArrays.read(program, programCounter, length);
        programCounter += length;
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

    public void memoryWrite(int index, int length, long value) {
        ByteArrays.write(memory, memoryOffset + index, length, value);
    }

    public long memoryRead(int index, int length) {
        return ByteArrays.read(memory, memoryOffset + index, length);
    }

    public void initialize(byte[] program, int stackSize, int memorySize) {
        programCounter = 0;
        stackPointer = 0;
        stack = new byte[stackSize];
        memory = new byte[memorySize];
        memoryOffset = 0;
        memoryAllocStart = 0;
        this.program = program;
        this.callStack = new Stack<>();
        this.nativeFunctions = new HashMap<>();
    }

    public Map<Integer, Consumer<RSIOExecutor>> getNativeFunctions() {
        return nativeFunctions;
    }

    public void execute() {
        while (programCounter >= 0 && programCounter < program.length) {
            byte head = program[programCounter++];
            byte flags = program[programCounter++];

            switch (head) {
                case HEAD_NOP: break;
                case HEAD_CAST: cast((Byte.toUnsignedInt(flags) & 0xF0) >>> 4, flags & 0x0F); break;
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
                    int target = (int) readProgram(4);
                    if (pop(1) == 0) {
                        programCounter = target;
                    } break;
                case HEAD_EXIT: programCounter = program.length; break;

                case HEAD_CALL: call(); break;
                case HEAD_RETURN: functionReturn(); break;
                case HEAD_ALLOC: alloc((int) readProgram(4)); break;
            }
        }
    }

    public void load(int length) {
        int memoryLocation = (int) readProgram(4);
        long value = memoryRead(memoryLocation, length);
        push(length, value);
    }

    public void save(int length) {
        int memoryLocation = (int) readProgram(4);
        long value = peek(length);
        memoryWrite(memoryLocation, length, value);
//        ByteArrays.copy(stack, stackPointer - length, memory, memoryLocation + memoryOffset, length);
    }

    public void cast(int originType, int targetType) {
        if (DataType.INTEGER.code == originType && targetType == DataType.BOOLEAN.code) {
            push(1, pop(4) == 0 ? 0 : 1);
        }
    }

    public void call() {
        int functionId = (int) pop(4);
        if (nativeFunctions.containsKey(functionId)) {
            nativeFunctions.get(functionId).accept(this);
        } else {
            callStack.push(programCounter);
            callStack.push(memoryOffset);
            callStack.push(memoryAllocStart);
            programCounter = functionId;
            memoryOffset = memoryAllocStart;
        }
    }

    public void functionReturn() {
        memoryAllocStart = callStack.pop();
        memoryOffset = callStack.pop();
        programCounter = callStack.pop();
    }

    public void alloc(int size) {
        memoryOffset = memoryAllocStart;
        memoryAllocStart += size;
    }

    public void mathematics(byte head, byte flags) {
        int operandLength = getLength(flags);

        if (head == Instruction.HEAD_NEG) {
            push(operandLength, Convert.convert(-pop(operandLength), operandLength));
            return;
        }

        long rightOperand = isInstant(flags) ? readProgram(operandLength) : pop(operandLength);
        long leftOperand = pop(operandLength);

        long result = 0;
        switch (head) {
            case Instruction.HEAD_PLUS: result = leftOperand + rightOperand; break;
            case HEAD_SUB: result = leftOperand - rightOperand; break;
            case Instruction.HEAD_MUL: result = leftOperand * rightOperand; break;
            case Instruction.HEAD_DIV: result = leftOperand / rightOperand; break;
            case Instruction.HEAD_MOD: result = leftOperand % rightOperand; break;
            case Instruction.HEAD_POW: result = (long) Math.pow(leftOperand, rightOperand); break;
        }

        push(operandLength, Convert.convert(result, operandLength));
    }

    public void compare(byte head, byte flags) {
        int operandLength = getLength(flags);

        long rightOperand = isInstant(flags) ? readProgram(operandLength) : pop(operandLength);
        long leftOperand = pop(operandLength);

        boolean result = false;
        switch (head) {
            case Instruction.HEAD_EQ: result = leftOperand == rightOperand; break;
            case Instruction.HEAD_NE: result = leftOperand != rightOperand; break;
            case Instruction.HEAD_GT: result = leftOperand > rightOperand; break;
            case Instruction.HEAD_LT: result = leftOperand < rightOperand; break;
            case Instruction.HEAD_GE: result = leftOperand >= rightOperand; break;
            case Instruction.HEAD_LE:  result = leftOperand <= rightOperand; break;
        }

        push(1, result ? 1 : 0);
    }

    public void logic(byte head, byte flags) {
        int operandLength = getLength(flags);

        if (head == Instruction.HEAD_NOT) {
            push(1, pop(1) == 0 ? 1 : 0);
            return;
        }

        long rightOperand = isInstant(flags) ? readProgram(operandLength) : pop(operandLength);
        long leftOperand = pop(operandLength);

        boolean result = false;
        switch (head) {
            case Instruction.HEAD_AND: result = leftOperand == rightOperand; break;
            case Instruction.HEAD_OR: result = leftOperand != rightOperand; break;
            case Instruction.HEAD_GT: result = leftOperand > rightOperand; break;
        }

        push(1, result ? 1 : 0);
    }
}

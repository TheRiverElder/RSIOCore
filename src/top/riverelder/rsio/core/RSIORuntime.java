package top.riverelder.rsio.core;

import top.riverelder.rsio.core.util.BytesReader;
import top.riverelder.rsio.core.util.BytesWriter;
import top.riverelder.rsio.core.util.Memory;

import java.util.Stack;

public class RSIORuntime {

    private final Memory memory;
    private final Stack<Integer> stack = new Stack<>();

    public RSIORuntime(int memorySize) {
        memory = new Memory(memorySize);
    }

    public Memory getMemory() {
        return memory;
    }

    public void pushInt(int value) {
        stack.push(value);
    }

    public int popInt() {
        return stack.pop();
    }

    public void printStack() {
        System.out.println(stack);
    }
}

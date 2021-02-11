package top.riverelder.rsio.core;

import java.util.Stack;

public class RSIORuntime {

    private final Stack<Integer> stack = new Stack<>();

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

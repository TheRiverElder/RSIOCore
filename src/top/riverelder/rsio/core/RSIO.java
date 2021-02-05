package top.riverelder.rsio.core;

import top.riverelder.rsio.core.util.BytesReader;

import static top.riverelder.rsio.core.Instructions.*;

public class RSIO {

    public static void run(byte[] bytes, RSIORuntime runtime) {
        BytesReader reader = new BytesReader(bytes);
        while (reader.hasByte()) {
            byte head = reader.readByte();
            switch (head) {
                case HEAD_NOP: break;

                case HEAD_LOAD: runtime.pushInt(runtime.getMemory().at(runtime.popInt()).readInt()); break;
                case HEAD_SAVE:

                case HEAD_PLUS:
                case HEAD_SUB:
                case HEAD_MUL:
                case HEAD_DIV:
                case HEAD_MOD:
                case HEAD_POW:
                case HEAD_AND:
                case HEAD_OR:

                case HEAD_EQ:
                case HEAD_NE:
                case HEAD_GT:
                case HEAD_LT:
                case HEAD_GE:
                case HEAD_LE:
                    executeBinaryExpression(runtime, head); break;

                case HEAD_NEG:
                case HEAD_NOT:
                    executeUnaryExpression(runtime, head); break;

                case HEAD_PUSH: runtime.pushInt(reader.readInt()); break;
                case HEAD_POP: runtime.popInt(); break;
                case HEAD_JMP: reader.setCursor(runtime.popInt()); break;
                case HEAD_IZJ: {
                    int target = runtime.popInt();
                    if (runtime.popInt() == 0) {
                        reader.setCursor(target);
                    }
                } break;
            }
            System.out.println(head);
            runtime.printStack();
        }
    }

    private static void executeBinaryExpression(RSIORuntime runtime, byte head) {
        int b = runtime.popInt();
        int a = runtime.popInt();
        int value = 0;
        switch (head) {
            case HEAD_SAVE: {
                runtime.getMemory().at(b).writeInt(a);
                value = a;
            } break;

            case HEAD_PLUS: value = a + b; break;
            case HEAD_SUB: value = a - b; break;
            case HEAD_MUL: value = a * b; break;
            case HEAD_DIV: value = a / b; break;
            case HEAD_MOD: value = a % b; break;
            case HEAD_POW: value = (int) Math.pow(a, b); break;

            case HEAD_AND: value = (a != 0 && b != 0) ? 1 : 0; break;
            case HEAD_OR: value = (a != 0 || b != 0) ? 1 : 0; break;

            case HEAD_EQ: value = (a == b) ? 1 : 0; break;
            case HEAD_NE: value = (a != b) ? 1 : 0; break;
            case HEAD_GT: value = (a > b) ? 1 : 0; break;
            case HEAD_LT: value = (a < b) ? 1 : 0; break;
            case HEAD_GE: value = (a >= b) ? 1 : 0; break;
            case HEAD_LE: value = (a <= b) ? 1 : 0; break;
        }
        runtime.pushInt(value);
    }

    private static void executeUnaryExpression(RSIORuntime runtime, byte head) {
        int a = runtime.popInt();
        int value = 0;
        switch (head) {
            case HEAD_NEG: value = -a; break;
            case HEAD_NOT: value = a == 0 ? 1 : 0; break;
        }
        runtime.pushInt(value);
    }

}

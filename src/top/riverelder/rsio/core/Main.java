package top.riverelder.rsio.core;

import top.riverelder.rsio.core.assemble.Assembler;
import top.riverelder.rsio.core.ast.AST;
import top.riverelder.rsio.core.bytecode.RSIOExecutor;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.RSIOParser;
import top.riverelder.rsio.core.compile.RootCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.token.Token;
import top.riverelder.rsio.core.util.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        testConvert();
        String name = "code_01";
        testCompiler(name);
        testAssemble(name);
    }

    public static void testConvert() {
        List<Long> originValues = new ArrayList<>();
        List<Long> targetValues = new ArrayList<>();
        List<Integer> lengths = new ArrayList<>();

        originValues.add(123456L);
        lengths.add(4);
        targetValues.add(123456L);

        originValues.add(-123456L);
        lengths.add(4);
        targetValues.add(Integer.toUnsignedLong(-123456));

        for (int i = 0; i < originValues.size(); i++) {
            long ov = originValues.get(i);
            int len = lengths.get(i);
            long tv = targetValues.get(i);
            long rv = Convert.convert(ov, len);
            System.out.printf("convert: %s, length: %s,expect: %s, get: %s, match: %s%n", Long.toBinaryString(ov), len, Long.toBinaryString(tv), Long.toBinaryString(rv), rv == tv);
        }
    }

    public static void testCompiler(String name) {
        String code = readFile(name + ".rsio");

        System.out.println("====CODE====");
        System.out.println(code);
        System.out.println("====RSIO====");
        try {
            RootCompileEnvironment env = new RootCompileEnvironment();
            env.createField("north", DataType.INTEGER, false);
            env.createField("south", DataType.INTEGER, false);
            env.createField("west", DataType.INTEGER, false);
            env.createField("east", DataType.INTEGER, false);
            env.createField("up", DataType.INTEGER, false);
            env.createField("down", DataType.INTEGER, false);
            env.createField("curTick", DataType.INTEGER, true);

            env.putDataType(DataType.BOOLEAN);
            env.putDataType(DataType.DECIMAL);
            env.putDataType(DataType.FUNCTION);
            env.putDataType(DataType.INTEGER);
            env.putDataType(DataType.VOID);
            env.putDataType(DataType.STRING);

            StaticStringReader reader = new StaticStringReader(code);
            RSIOTokenizer tokenizer = new RSIOTokenizer(reader);
            List<Token> tokens = tokenizer.tokenize();
            tokens.forEach(System.out::println);

            TokenReader tokenReader = new TokenReader(tokens);
            RSIOParser parser = new RSIOParser(tokenReader);
            AST program = parser.parse();
            BufferedStringBuilder sourceCodeBuilder = new BufferedStringBuilder();
            program.toSource(sourceCodeBuilder);
            System.out.println(sourceCodeBuilder);

            env.getFieldList().forEach(System.out::println);

            List<String> assembleCodes = new ArrayList<>();
            program.toAssemble(assembleCodes, env);
            assembleCodes.forEach(System.out::println);
            String assemble = String.join("\n", assembleCodes);
            writeFile(name + ".rsio.asm", assemble);

//            byte[] bytes = RSIO.compile(code, env);
//
//            RSIORuntime runtime = new RSIORuntime(6 * 4);
//            runtime.getMemory().at(env.getField("north").position).writeInt(56);
//
//            RSIO.run(bytes, runtime);
//            runtime.getMemory().print();
        } catch (RSIOCompileException e) {
            System.err.println(e.getMessage());
            int position = e.getPosition();
            System.err.println(
                    (position == 0 ? "" : "...")
                            + code.substring(position, Math.min(position + 10, code.length()))
                            + (position + 10 < code.length() ? "..." : ""));
        }
    }

    public static void testAssemble(String name) {
        Assembler assembler = new Assembler();
        byte[] bytecode = assembler.toBytes(readFile(name + ".rsio.asm"));
        ByteArrays.print(bytecode, 4);
        RSIOExecutor executor = new RSIOExecutor();
        executor.initialize(bytecode, 32, 32);
        executor.execute();
        System.out.println("--------");
        ByteArrays.print(executor.getMemory(), 4);
    }

    public static String readFile(String fileName) {
        StringBuilder builder= new StringBuilder();
        char[] buf = new char[1024];
        int len;
        try (FileReader reader = new FileReader(new File("./test/" + fileName))) {
            while ((len = reader.read(buf)) > 0) {
                builder.append(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void writeFile(String fileName, String data) {
        try (FileWriter writer = new FileWriter("./test/" + fileName)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

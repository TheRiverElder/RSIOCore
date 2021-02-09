package top.riverelder.rsio.core;

import top.riverelder.rsio.core.ast.AST;
import top.riverelder.rsio.core.compile.NestedCompileEnvironment;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.compile.RSIOParser;
import top.riverelder.rsio.core.compile.RootCompileEnvironment;
import top.riverelder.rsio.core.exception.RSIOCompileException;
import top.riverelder.rsio.core.token.Token;
import top.riverelder.rsio.core.util.BufferedStringBuilder;
import top.riverelder.rsio.core.util.StaticStringReader;
import top.riverelder.rsio.core.util.TokenReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String code = "";
        try (FileReader reader = new FileReader(new File("./test/code_03.txt"))) {
            StringBuilder builder= new StringBuilder();
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) > 0) {
                builder.append(buf, 0, len);
            }
            code = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }



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

}

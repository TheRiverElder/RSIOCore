package top.riverelder.rsio.core;

import top.riverelder.rsio.core.ast.AST;
import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String code = "";
        try (FileReader reader = new FileReader(new File("./test/code_01.txt"))) {
            StringBuilder builder= new StringBuilder();
            char[] buf = new char[1024];
            int len = 0;
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
            CompileEnvironment env = new CompileEnvironment();
            env.createField("north", DataType.INTEGER);
            env.createField("south", DataType.INTEGER);
            env.createField("west", DataType.INTEGER);
            env.createField("east", DataType.INTEGER);
            env.createField("up", DataType.INTEGER);
            env.createField("down", DataType.INTEGER);

            byte[] bytes = RSIO.compile(code, env);

            RSIORuntime runtime = new RSIORuntime(6 * 4);
            runtime.getMemory().at(env.getField("north").position).writeInt(56);

            RSIO.run(bytes, runtime);
            runtime.getMemory().print();
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

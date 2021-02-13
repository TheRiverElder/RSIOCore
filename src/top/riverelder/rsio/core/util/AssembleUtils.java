package top.riverelder.rsio.core.util;

import top.riverelder.rsio.core.compile.DataType;
import top.riverelder.rsio.core.exception.RSIOCompileException;

import java.util.List;

public class AssembleUtils {

    public static void checkAndCast(List<String> output, DataType origin, DataType target) {
        if (origin != target) {
            output.add("  cast." + ((origin.code << 4) + target.code));
        }
    }

}

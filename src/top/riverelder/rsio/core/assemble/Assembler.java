package top.riverelder.rsio.core.assemble;

import top.riverelder.rsio.core.bytecode.Instruction;
import top.riverelder.rsio.core.util.ByteArrays;
import top.riverelder.rsio.core.util.Convert;

import java.util.*;
import java.util.stream.Collectors;

public class Assembler {

    private static final Set<Instruction> INSTRUCTIONS_WITH_PARAMETERS = new HashSet<>(Arrays.asList(
            Instruction.ALLOC, Instruction.LOAD, Instruction.PUSH, Instruction.SAVE
    ));

    public byte[] toBytes(String code) {
        List<String> lines = Arrays.stream(code.split("\\r?\\n"))
                .map(String::trim)
                .filter(l -> !l.matches("^\\s*$"))
                .collect(Collectors.toList());

        Map<String, Integer> functionIds = new HashMap<>();

        List<Byte> bytes = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            String part0 = parts[0];

            if (Objects.equals(part0, "sect")) {
                functionIds.put(parts[1], bytes.size());
                continue;
            } else if (Objects.equals(part0, "end")) {
                continue;
            }

            String[] headParts = part0.split("\\.");
            String head = headParts[0].toUpperCase();
            byte flags = headParts.length > 1 ? Byte.valueOf(headParts[1]) : 0;

            long parameter = parts.length > 1 ? Long.parseLong(parts[1]) : 0;

            Instruction instruction = Instruction.valueOf(head);
            bytes.add(instruction.head);
            bytes.add(flags);
            if (INSTRUCTIONS_WITH_PARAMETERS.contains(instruction)) {
                byte[] buf = new byte[8];
                ByteArrays.write(buf, 0, flags, Convert.convert(parameter, flags));
                for (int i = 0; i < flags; i++) {
                    bytes.add(buf[i]);
                }
            }
        }

        byte[] bytecode = new byte[bytes.size()];
        for (int i = 0; i < bytecode.length; i++) {
            bytecode[i] = bytes.get(i);
        }
        return bytecode;
    }

}

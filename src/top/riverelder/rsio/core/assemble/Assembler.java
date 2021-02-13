package top.riverelder.rsio.core.assemble;

import top.riverelder.rsio.core.bytecode.Instruction;
import top.riverelder.rsio.core.util.ByteArrays;
import top.riverelder.rsio.core.util.Convert;

import java.util.*;
import java.util.stream.Collectors;

public class Assembler {

    private static final Set<Instruction> INSTRUCTIONS_WITH_PARAMETERS = new HashSet<>(Arrays.asList(
            Instruction.ALLOC, Instruction.LOAD, Instruction.PUSH, Instruction.SAVE, Instruction.JMP, Instruction.IZJ
    ));

    public byte[] toBytes(String code) {
        List<String> lines = Arrays.stream(code.split("\\r?\\n"))
                .map(String::trim)
                .filter(l -> !l.matches("^\\s*$"))
                .collect(Collectors.toList());

        Map<String, Integer> functionIds = new HashMap<>();
        Map<String, Integer> labels = new HashMap<>();
        Map<Integer, String> unfilledLabels = new HashMap<>();

        List<Byte> bytes = new ArrayList<>();
        for (String line : lines) {
            if (line.endsWith(":")) {
                labels.put(line.substring(0, line.length() - 1), bytes.size());
                continue;
            }
            String[] parts = line.split("\\s+");
            String part0 = parts[0];

            if (Objects.equals(part0, "sect")) {
                functionIds.put(parts[1], bytes.size());
                continue;
            } else if (Objects.equals(part0, "end")) {
                bytes.add(Instruction.RETURN.head);
                bytes.add((byte) 0);
                continue;
            }

            // 处理指令头
            String[] headParts = part0.split("\\.");
            String head = headParts[0].toUpperCase();
            byte flags = headParts.length > 1 ? Byte.valueOf(headParts[1]) : 0;
            // 写入指令头数据
            Instruction instruction = Instruction.valueOf(head);
            if (instruction == Instruction.JMP || instruction == Instruction.IZJ) {
                flags = 4;
            }
            bytes.add(instruction.head);
            bytes.add(flags);

            long parameter = 0;
            if (parts.length > 1) {
                String part1 = parts[1];
                if (part1.matches("^\\d+$")) {
                    parameter = Long.parseLong(part1);
                } else if (INSTRUCTIONS_WITH_PARAMETERS.contains(instruction)) {
                    unfilledLabels.put(bytes.size(), part1);
                }
            }

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

        for (Map.Entry<Integer, String> e: unfilledLabels.entrySet()){
            ByteArrays.write(bytecode, e.getKey(), 4, labels.get(e.getValue()));
        }

        return bytecode;
    }

}

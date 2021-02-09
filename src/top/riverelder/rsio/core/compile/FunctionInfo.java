package top.riverelder.rsio.core.compile;

import java.util.List;

public class FunctionInfo {

    public static class Parameter {
        public final String name;
        public final DataType dataType;

        public Parameter(String name, DataType dataType) {
            this.name = name;
            this.dataType = dataType;
        }
    }

    public final String name;
    public final DataType resultDataType;
    public final List<DataType> parameterDataTypes;

    public FunctionInfo(String name, DataType resultDataType, List<DataType> parameterDataTypes) {
        this.name = name;
        this.resultDataType = resultDataType;
        this.parameterDataTypes = parameterDataTypes;
    }
}

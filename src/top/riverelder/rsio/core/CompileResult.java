package top.riverelder.rsio.core;

import java.util.Arrays;

public class CompileResult {

    private boolean succeed;
    private byte[] bytes;
    private String errorMessage;
    private int errorPosition;

    public CompileResult(byte[] bytes) {
        this.succeed = true;
        this.bytes = bytes;
    }

    public CompileResult(String errorMessage, int errorPosition) {
        this.succeed = false;
        this.errorMessage = errorMessage;
        this.errorPosition = errorPosition;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorPosition() {
        return errorPosition;
    }

    @Override
    public String toString() {
        return "CompileResult{" +
                ", succeed=" + succeed +
                "bytes=" + Arrays.toString(bytes) +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorPosition=" + errorPosition +
                '}';
    }
}

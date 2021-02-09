package top.riverelder.rsio.core.exception;

public class RSIOCompileException extends Exception {

    private int position;

    public RSIOCompileException(String message, int position) {
        super(message + " @ " + position);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}

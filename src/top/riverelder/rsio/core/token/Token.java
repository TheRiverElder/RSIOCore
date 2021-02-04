package top.riverelder.rsio.core.token;

import top.riverelder.rsio.core.util.IToBytes;
import top.riverelder.rsio.core.util.IToSource;

public abstract class Token implements IToSource {

    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public abstract TokenType getType();

    public abstract Object getContent();

    @Override
    public String toString() {
        String s = String.format("%s@%d{ %s }", getType(), position, getContent());
//        return getType() + "@" + position + "{ " + getContent() + " }";
        return s;
    }
}

package top.riverelder.rsio.core.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StaticStringReader {

    private String str;
    private int cursor;

    public StaticStringReader(String str) {
        this.str = str;
        this.cursor = 0;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getCursor() {
        return cursor;
    }

    public String getStr() {
        return str;
    }

    public boolean hasMore() {
        return cursor < str.length();
    }

    public void skipWhitespace() {
        while (hasMore() && Character.isWhitespace(peek())) {
            cursor++;
        }
    }

    public char peek() {
        return str.charAt(cursor);
    }

    public char read() {
        return str.charAt(cursor++);
    }

    public boolean read(char ch) {
        if (!hasMore() || peek() != ch) return false;

        cursor++;
        return true;
    }

    public boolean read(String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            int nc = cursor + i;
            if (nc >= str.length() || str.charAt(nc) != pattern.charAt(i)) return false;
        }
        cursor += pattern.length();
        return true;
    }

    public String read(String ...patterns) {
        List<String> pats = Arrays.stream(patterns).sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
        Collections.reverse(pats);
        for (String pat : pats) {
            if (read(pat)) return pat;
        }
        return null;
    }

    public String readFollowing(Predicate<Character> predicate) {
        int start = cursor;
        while (hasMore() && predicate.test(peek())) {
            cursor++;
        }
        return start == cursor ? null : str.substring(start, cursor);
    }

    public String readUntil(Predicate<Character> predicate) {
        int start = cursor;
        while (hasMore() && !predicate.test(peek())) {
            cursor++;
        }
        return start == cursor ? null : str.substring(start, cursor);
    }
}

package com.winzig.lexical.analyzer;

public class Token_Integer extends Token {

    private int value;

    public Token_Integer(int value) {
        this.value = value;
    }

    public Token_Integer() {}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token_Integer{" +
                "value=" + value +
                '}';
    }
}

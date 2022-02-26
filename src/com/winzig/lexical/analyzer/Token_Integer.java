package com.winzig.lexical.analyzer;

public class Token_Integer extends Token {

    public String name = "integer";

    public int value;

    public Token_Integer(int value) {
        this.value = value;
    }

    public Token_Integer() {}

    @Override
    public String toString() {
        return "Token_Integer{" +
                "value=" + value +
                '}';
    }

    @Override
    public String getName() {
        return "integer";
    }
}

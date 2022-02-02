package com.winzig.lexical.analyzer;

public class Token_Identifier extends Token {

    private String value;

    public Token_Identifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token_Identifier{" +
                "value='" + value + '\'' +
                '}';
    }
}

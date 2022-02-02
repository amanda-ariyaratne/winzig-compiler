package com.winzig.lexical.analyzer;

public class Token_String extends Token {

    private String value;

    public Token_String(String value) {
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
        return "Token_String{" +
                "value='" + value + '\'' +
                '}';
    }
}

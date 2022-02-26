package com.winzig.lexical.analyzer;

public class Token_String extends Token {

    public String name = "string";

    public String value;

    public Token_String(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token_String{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }
}

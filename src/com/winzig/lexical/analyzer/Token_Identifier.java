package com.winzig.lexical.analyzer;

public class Token_Identifier extends Token {

    public String name = "identifier";

    public String value;

    public Token_Identifier(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token_Identifier{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_GT extends Token {

    public String name = ">";

    @Override
    public String toString() {
        return "Token_GT{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

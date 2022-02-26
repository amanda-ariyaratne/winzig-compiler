package com.winzig.lexical.analyzer;

public class Token_Case extends Token {

    public String name = "case";

    @Override
    public String toString() {
        return "Token_Case{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Minus extends Token {

    public String name = "-";
    @Override
    public String toString() {
        return "Token_Minus{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

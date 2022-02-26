package com.winzig.lexical.analyzer;

public class Token_Otherwise extends Token {

    public String name = "otherwise";

    @Override
    public String toString() {
        return "Token_Otherwise{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

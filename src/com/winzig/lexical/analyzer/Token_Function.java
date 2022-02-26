package com.winzig.lexical.analyzer;

public class Token_Function extends Token {

    public String name = "function";

    @Override
    public String toString() {
        return "Token_Function{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

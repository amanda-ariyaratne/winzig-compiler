package com.winzig.lexical.analyzer;

public class Token_True extends Token {

    public String name = "true";

    @Override
    public String toString() {
        return "Token_True{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

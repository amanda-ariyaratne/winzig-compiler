package com.winzig.lexical.analyzer;

public class Token_Loop extends Token {

    public String name = "loop";

    @Override
    public String toString() {
        return "Token_Loop{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Call extends Token {

    public String name = "call";

    @Override
    public String toString() {
        return "Token_Call{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

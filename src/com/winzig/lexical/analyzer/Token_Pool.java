package com.winzig.lexical.analyzer;

public class Token_Pool extends Token {

    public String name = "pool";

    @Override
    public String toString() {
        return "Token_Pool{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

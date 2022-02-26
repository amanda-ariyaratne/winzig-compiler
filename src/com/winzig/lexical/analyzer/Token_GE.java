package com.winzig.lexical.analyzer;

public class Token_GE extends Token {

    public String name = ">=";

    @Override
    public String toString() {
        return "Token_GE{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

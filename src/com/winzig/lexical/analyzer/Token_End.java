package com.winzig.lexical.analyzer;

public class Token_End extends Token {

    public String name = "end";

    @Override
    public String toString() {
        return "Token_End{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

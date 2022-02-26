package com.winzig.lexical.analyzer;

public class Token_While extends Token {

    public String name = "while";

    @Override
    public String toString() {
        return "Token_While{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

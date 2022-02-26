package com.winzig.lexical.analyzer;

public class Token_If extends Token {

    public String name = "if";

    @Override
    public String toString() {
        return "Token_If{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

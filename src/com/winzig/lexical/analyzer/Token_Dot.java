package com.winzig.lexical.analyzer;

public class Token_Dot extends Token {

    public String name = ".";

    @Override
    public String toString() {
        return "Token_Dot{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

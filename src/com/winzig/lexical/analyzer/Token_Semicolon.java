package com.winzig.lexical.analyzer;

public class Token_Semicolon extends Token {

    public String name = ";";

    @Override
    public String toString() {
        return "Token_Semicolon{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

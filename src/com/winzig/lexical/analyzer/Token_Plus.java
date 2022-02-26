package com.winzig.lexical.analyzer;

public class Token_Plus extends Token {

    public String name = "+";

    @Override
    public String toString() {
        return "Token_Plus{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

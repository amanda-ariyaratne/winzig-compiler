package com.winzig.lexical.analyzer;

public class Token_Not extends Token {

    public String name = "not";

    @Override
    public String toString() {
        return "Token_Not{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

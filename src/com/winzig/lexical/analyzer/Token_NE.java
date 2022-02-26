package com.winzig.lexical.analyzer;

public class Token_NE extends Token {

    public String name = "<>";

    @Override
    public String toString() {
        return "Token_NE{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

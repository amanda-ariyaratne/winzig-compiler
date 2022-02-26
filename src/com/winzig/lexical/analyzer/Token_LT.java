package com.winzig.lexical.analyzer;

public class Token_LT extends Token {

    public String name = "<";

    @Override
    public String toString() {
        return "Token_LT{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

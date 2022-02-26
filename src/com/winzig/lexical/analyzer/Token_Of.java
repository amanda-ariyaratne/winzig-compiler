package com.winzig.lexical.analyzer;

public class Token_Of extends Token {

    public String name = "of";

    @Override
    public String toString() {
        return "Token_Of{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Then extends Token {

    public String name = "then";

    @Override
    public String toString() {
        return "Token_Then{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

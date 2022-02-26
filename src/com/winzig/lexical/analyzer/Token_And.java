package com.winzig.lexical.analyzer;

public class Token_And extends Token {

    public String name = "and";

    @Override
    public String toString() {
        return "Token_And{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

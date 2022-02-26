package com.winzig.lexical.analyzer;

public class Token_Equal extends Token {

    public String name = "=";

    @Override
    public String toString() {
        return "Token_Equal{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

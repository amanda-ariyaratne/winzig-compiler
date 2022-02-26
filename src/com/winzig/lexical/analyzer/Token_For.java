package com.winzig.lexical.analyzer;

public class Token_For extends Token {

    public String name = "for";

    @Override
    public String toString() {
        return "Token_For{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

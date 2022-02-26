package com.winzig.lexical.analyzer;

public class Token_Close_Bracket extends Token {

    public String name = ")";

    @Override
    public String toString() {
        return "Token_Close_Bracket{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

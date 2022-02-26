package com.winzig.lexical.analyzer;

public class Token_Swap extends Token {

    public String name = ":=:";

    @Override
    public String toString() {
        return "Token_Swap{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

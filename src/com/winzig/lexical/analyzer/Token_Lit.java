package com.winzig.lexical.analyzer;

public class Token_Lit extends Token {

    public String name = "lit";

    @Override
    public String toString() {
        return "Token_Lit{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Succ extends Token {

    public String name = "succ";

    @Override
    public String toString() {
        return "Token_Succ{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

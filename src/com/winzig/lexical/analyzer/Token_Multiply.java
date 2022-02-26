package com.winzig.lexical.analyzer;

public class Token_Multiply extends Token {

    public String name = "*";

    @Override
    public String toString() {
        return "Token_Multiply{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

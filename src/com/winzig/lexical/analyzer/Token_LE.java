package com.winzig.lexical.analyzer;

public class Token_LE extends Token {

    public String name = "<=";

    @Override
    public String toString() {
        return "Token_LE{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

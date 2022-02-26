package com.winzig.lexical.analyzer;

public class Token_Divide extends Token {

    public String name = "/";

    @Override
    public String toString() {
        return "Token_Divide{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

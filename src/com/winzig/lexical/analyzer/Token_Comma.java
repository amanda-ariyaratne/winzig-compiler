package com.winzig.lexical.analyzer;

public class Token_Comma extends Token {

    public String name = "comma";

    @Override
    public String toString() {
        return "Token_Comma{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

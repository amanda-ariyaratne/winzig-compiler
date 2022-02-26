package com.winzig.lexical.analyzer;

public class Token_Open_Bracket extends Token {

    public String name = "(";

    @Override
    public String toString() {
        return "Token_Open_Bracket{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

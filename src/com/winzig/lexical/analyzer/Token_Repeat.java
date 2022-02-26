package com.winzig.lexical.analyzer;

public class Token_Repeat extends Token {

    public String name = "repeat";

    @Override
    public String toString() {
        return "Token_Repeat{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

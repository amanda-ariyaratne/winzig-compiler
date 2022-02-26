package com.winzig.lexical.analyzer;

public class Token_Newline extends Token {

    public String name = "newline";

    @Override
    public String toString() {
        return "Token_Newline{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

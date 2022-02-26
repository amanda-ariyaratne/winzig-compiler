package com.winzig.lexical.analyzer;

public class Token_Null extends Token {

    public String name = "<null>";

    @Override
    public String toString() {
        return "Token_Null{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

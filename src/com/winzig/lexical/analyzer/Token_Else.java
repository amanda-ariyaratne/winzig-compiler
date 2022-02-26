package com.winzig.lexical.analyzer;

public class Token_Else extends Token {

    public String name = "else";

    @Override
    public String toString() {
        return "Token_Else{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Mod extends Token {

    public String name = "mod";

    @Override
    public String toString() {
        return "Token_Mod{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

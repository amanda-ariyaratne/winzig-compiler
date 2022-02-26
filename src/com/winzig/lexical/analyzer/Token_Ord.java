package com.winzig.lexical.analyzer;

public class Token_Ord extends Token {

    public String name = "ord";

    @Override
    public String toString() {
        return "Token_Ord{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Colon extends Token {

    public String name = "colon";

    @Override
    public String toString() {
        return "Token_Colon{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

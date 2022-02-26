package com.winzig.lexical.analyzer;

public class Token_Dcln extends Token {

    public String name = "dcln";

    @Override
    public String toString() {
        return "Token_Dcln{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

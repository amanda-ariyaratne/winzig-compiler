package com.winzig.lexical.analyzer;

public class Token_Output extends Token {

    public String name = "output";

    @Override
    public String toString() {
        return "Token_Output{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

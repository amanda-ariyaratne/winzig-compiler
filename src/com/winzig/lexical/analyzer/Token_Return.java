package com.winzig.lexical.analyzer;

public class Token_Return extends Token {

    public String name = "return";

    @Override
    public String toString() {
        return "Token_Return{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

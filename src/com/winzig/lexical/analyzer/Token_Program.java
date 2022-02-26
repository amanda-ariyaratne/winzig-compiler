package com.winzig.lexical.analyzer;

public class Token_Program extends Token {

    public String name = "program";

    @Override
    public String toString() {
        return "Token_Program{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Var extends Token {

    public String name = "var";

    @Override
    public String toString() {
        return "Token_Var{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

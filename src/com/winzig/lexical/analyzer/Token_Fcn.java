package com.winzig.lexical.analyzer;

public class Token_Fcn extends Token {

    public String name = "fcn";

    @Override
    public String toString() {
        return "Token_Fcn{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

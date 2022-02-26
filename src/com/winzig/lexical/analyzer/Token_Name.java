package com.winzig.lexical.analyzer;

public class Token_Name extends Token {

    public String name = "name";

    @Override
    public String toString() {
        return "Token_Name{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

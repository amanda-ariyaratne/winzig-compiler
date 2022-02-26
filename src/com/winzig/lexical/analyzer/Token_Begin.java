package com.winzig.lexical.analyzer;

public class Token_Begin extends Token {

    public String name = "begin";

    @Override
    public String toString() {
        return "Token_Begin{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

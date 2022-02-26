package com.winzig.lexical.analyzer;

public class Token_Begin_Block extends Token {

    public String name = "{";

    @Override
    public String toString() {
        return "Token_Begin_Block{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

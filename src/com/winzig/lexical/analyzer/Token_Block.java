package com.winzig.lexical.analyzer;

public class Token_Block extends Token {

    public String name = "block";

    @Override
    public String toString() {
        return "Token_Block{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

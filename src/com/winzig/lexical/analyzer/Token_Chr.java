package com.winzig.lexical.analyzer;

public class Token_Chr extends Token {

    public String name = "chr";

    @Override
    public String toString() {
        return "Token_Chr{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

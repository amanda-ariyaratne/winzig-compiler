package com.winzig.lexical.analyzer;

public class Token_Do extends Token {

    public String name = "do";

    @Override
    public String toString() {
        return "Token_Do{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

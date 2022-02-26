package com.winzig.lexical.analyzer;

public class Token_Const extends Token {

    public String name = "const";

    @Override
    public String toString() {
        return "Token_Const{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

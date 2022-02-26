package com.winzig.lexical.analyzer;

public class Token_Consts extends Token{

    public String name = "consts";

    @Override
    public String toString() {
        return "Token_Consts{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

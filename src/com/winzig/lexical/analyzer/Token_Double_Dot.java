package com.winzig.lexical.analyzer;

public class Token_Double_Dot extends Token {

    public String name = "..";

    @Override
    public String toString() {
        return "Token_Double_Dot{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

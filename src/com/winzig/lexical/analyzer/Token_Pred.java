package com.winzig.lexical.analyzer;

public class Token_Pred extends Token {

    public String name = "pred";

    @Override
    public String toString() {
        return "Token_Pred{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

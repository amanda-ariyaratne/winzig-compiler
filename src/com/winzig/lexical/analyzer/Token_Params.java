package com.winzig.lexical.analyzer;

public class Token_Params extends Token {

    public String name = "params";

    @Override
    public String toString() {
        return "Token_Params{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

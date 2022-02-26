package com.winzig.lexical.analyzer;

public class Token_Types extends Token {

    public String name = "types";

    @Override
    public String toString() {
        return "Token_Types{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

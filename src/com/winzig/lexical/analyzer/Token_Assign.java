package com.winzig.lexical.analyzer;

public class Token_Assign extends Token {

    public String name = "assign";

    @Override
    public String toString() {
        return "Token_Assign{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

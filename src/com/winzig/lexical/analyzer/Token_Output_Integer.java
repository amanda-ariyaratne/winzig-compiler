package com.winzig.lexical.analyzer;

public class Token_Output_Integer extends Token {
    public String name = "integer";

    @Override
    public String toString() {
        return "Token_Output_Integer{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

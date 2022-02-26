package com.winzig.lexical.analyzer;

public class Token_Read extends Token {

    public String name = "read";

    @Override
    public String toString() {
        return "Token_Read{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

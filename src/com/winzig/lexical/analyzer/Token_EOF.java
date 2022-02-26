package com.winzig.lexical.analyzer;

public class Token_EOF extends Token {

    public String name = "eof";

    @Override
    public String toString() {
        return "Token_EOF{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

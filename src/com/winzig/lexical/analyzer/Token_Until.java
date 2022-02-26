package com.winzig.lexical.analyzer;

public class Token_Until extends Token {

    public String name = "until";

    @Override
    public String toString() {
        return "Token_Until{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

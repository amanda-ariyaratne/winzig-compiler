package com.winzig.lexical.analyzer;

public class Token_Or extends Token {

    public String name ="or";

    @Override
    public String toString() {
        return "Token_Or{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

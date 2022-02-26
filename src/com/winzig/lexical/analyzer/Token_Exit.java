package com.winzig.lexical.analyzer;

public class Token_Exit extends Token {

    public String name = "exit";

    @Override
    public String toString() {
        return "Token_Exit{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Type extends Token {

    public String name = "type";

    @Override
    public String toString() {
        return "Token_Type{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.winzig.lexical.analyzer;

public class Token_Dclns extends Token {

    public String name = "dclns";

    @Override
    public String toString() {
        return "Token_Dclns{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

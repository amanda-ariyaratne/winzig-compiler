package com.winzig.lexical.analyzer;

public class Token_Subprogs extends Token {

    public String name = "subprogs";

    @Override
    public String toString() {
        return "Token_Subprogs{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

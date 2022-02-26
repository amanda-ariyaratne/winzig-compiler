package com.winzig.lexical.analyzer;

public class Token_Case_Clause extends Token {

    public String name = "case_clause";

    @Override
    public String toString() {
        return "Token_Case_Clause{}";
    }

    @Override
    public String getName() {
        return name;
    }
}

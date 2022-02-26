package com.winzig.lexical.analyzer;

public class Token_Char extends Token {

    public String name = "char";

    public char value;

    public Token_Char(char letter) {
        value = letter;
    }

    @Override
    public String toString() {
        return "Token_Char{" +
                "value=" + value +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }
}

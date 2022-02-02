package com.winzig.lexical.analyzer;

public class Token_Char extends Token {

    private char value;

    public Token_Char(char letter) {
        value = letter;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token_Char{" +
                "value=" + value +
                '}';
    }
}

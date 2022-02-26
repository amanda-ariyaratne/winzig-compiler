package com.winzig.lexical.analyzer;

public class Token_Comment extends Token {

    public String name = "comment";

    public String value;

    public Token_Comment(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token_Comment{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }
}

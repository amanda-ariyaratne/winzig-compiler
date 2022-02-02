package com.winzig.parser;

import com.winzig.lexical.analyzer.LexicalAnalyzer;
import com.winzig.lexical.analyzer.Token;

import java.util.ArrayList;

public class Parser {

    private LexicalAnalyzer lex;
    private ArrayList<Token> stack;

    public Parser(LexicalAnalyzer lex) {
        this.lex = lex;
        this.stack = new ArrayList<>();
    }

    public void parse() {

    }
}

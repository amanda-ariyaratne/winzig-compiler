package com.winzig;

import com.winzig.lexical.analyzer.LexicalAnalyzer;
import com.winzig.lexical.analyzer.Token;

import java.io.IOException;

public class winzig {

    public static void main(String[] args) {

        LexicalAnalyzer lex = new LexicalAnalyzer();
        try {
            lex.readInputProgram("winzig_15");
            Token token;
            while ((token = lex.getNextToken()) != null) {
                System.out.println(token);
            }
        } catch (IOException e) {
            System.out.println("Failed to read the input file.");
        }
    }
}

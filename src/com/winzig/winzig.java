package com.winzig;

import com.winzig.lexical.analyzer.LexicalAnalyzer;
import com.winzig.lexical.analyzer.Token;
import com.winzig.parser.Parser;

import java.io.IOException;

public class winzig {

    public static void main(String[] args) throws IOException {

        LexicalAnalyzer lex = new LexicalAnalyzer();
        lex.readInputProgram("winzig_01");
        Parser parser = new Parser(lex);
        parser.parse();
        System.out.println("\n\n\n");
        parser.traverseTree();

    }
}

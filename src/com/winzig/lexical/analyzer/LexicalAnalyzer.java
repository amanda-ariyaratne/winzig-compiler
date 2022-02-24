package com.winzig.lexical.analyzer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LexicalAnalyzer {

    private String input;
    private int lexemeBegin;
    private int forward;
    private int state;
    private final String[] reservedKeywords;

    public LexicalAnalyzer() {
        lexemeBegin = 0;
        forward = -1;
        reservedKeywords = new String[]{"program", "var", "const", "type", "function", "return", "begin", "end",
                "output", "if", "then", "else", "while", "do", "case", "of", "otherwise", "repeat", "for", "until",
                "loop", "pool", "exit", "mod", "and", "or", "not", "read", "succ", "pred", "chr", "ord", "eof"
        };
    }

    public void readInputProgram(String filename) throws IOException {
        Path path = Paths.get("/Users", "amanda-ariyaratne", "Downloads", "winzig_test_programs", filename);
        input = Files.readString(path, StandardCharsets.US_ASCII);
        input = input + (char) ASCII.NULL;
    }

    public Token readNextToken() {
        int tempLexemeBegin = lexemeBegin;
        int tempForward = forward;

        Token token = getNextToken();

        lexemeBegin = tempLexemeBegin;
        forward = tempForward;

        return token;
    }

    public Token getNextToken() {
        Token token = null;

        int tokenType = 0;
        while (token == null) {
            switch (++tokenType) {
                case 1: {
                    token = getIdentifier();
                    break;
                }
                case 2: {
                    token = getInteger();
                    break;
                }
                case 3: {
                    token = getWhitespace();
                    break;
                }
                case 4: {
                    token = getChar();
                    break;
                }
                case 5: {
                    token = getString();
                    break;
                }
                case 6: {
                    token = getComment();
                    break;
                }
                case 7: {
                    token = getColonOrSwapOrAssignment();
                    break;
                }
                case 8: {
                    token = getDotOrDoubleDot();
                    break;
                }
                case 9: {
                    token = getLEorLTorNE();
                    break;
                }
                case 10: {
                    token = getGEorGT();
                    break;
                }
                case 11: {
                    token = getEqual();
                    break;
                }
                case 12: {
                    token = getSemicolon();
                    break;
                }
                case 13: {
                    token = getComma();
                    break;
                }
                case 14: {
                    token = getOpenBracket();
                    break;
                }
                case 15: {
                    token = getCloseBracket();
                    break;
                }
                case 16: {
                    token = getPlus();
                    break;
                }
                case 17: {
                    token = getMinus();
                    break;
                }
                case 18: {
                    token = getMultiply();
                    break;
                }
                case 19: {
                    token = getDivide();
                    break;
                }
                default: {
                    int c = input.charAt(++forward);
                    if (c == ASCII.NULL) {
                        return null;
                    }
                }
            }
            if (token != null && (token instanceof Token_Comment || token instanceof Token_Whitespace)) {
                token = null;
                tokenType = 0;
            }
        }
        return token;
    }

    private Token getIdentifier() {
        state = 1;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 1: {
                    if (isLetterOrUnderscore(c)) { state = 2; }
                    else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 2: {
                    if (!isLetterOrDigitOrUnderscore(c)) { state = 3;}
                    else { break; }
                }
                case 3: {
                    forward--;
                    String identifier = input.substring(lexemeBegin, forward + 1);
                    lexemeBegin = forward + 1;

                    for (String reservedKeyword : this.reservedKeywords) {
                        if (reservedKeyword.equals(identifier)) {
                            return getReservedKeywordToken(reservedKeyword);
                        }
                    }

                    return new Token_Identifier(identifier);
                }
                default: { return null; }
            }
        }
    }

    private Token getInteger() {
        state = 4;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 4: {
                    if (c >= ASCII.ZERO && c <= ASCII.NINE) {
                        state = 5;
                    } else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 5: {
                    if (! (c >= ASCII.ZERO && c <= ASCII.NINE)) { state = 6; }
                    else { break; }
                }
                case 6: {
                    forward--;
                    Token token = new Token_Integer(Integer.parseInt(input.substring(lexemeBegin, forward + 1)));
                    lexemeBegin = forward + 1;
                    return token;
                }
                default: { return null; }
            }
        }
    }

    private Token getWhitespace() {
        state = 7;
        while (true) {
            int c = input.charAt(++forward);
            boolean b = (c == ASCII.SPACE)
                    || (c == ASCII.NEWLINE)
                    || (c == ASCII.FORM_FEED)
                    || (c == ASCII.HORIZONTAL_TAB)
                    || (c == ASCII.VERTICAL_TAB);
            switch (state) {
                case 7: {
                    if (b) {
                        state = 8;
                    } else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 8: {
                    if (!b) {
                        state = 9;
                    } else { break; }
                }
                case 9: {
                    forward--;
                    Token token = new Token_Whitespace(input.substring(lexemeBegin, forward + 1));
                    lexemeBegin = forward + 1;
                    return token;
                }
                default: { return null; }
            }
        }
    }

    private Token getChar() {
        state = 10;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 10: {
                    if (c == ASCII.SINGLE_QUOTE) {
                        state = 11;
                    } else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 11: {
                    if (c == ASCII.SINGLE_QUOTE) {
                        forward = lexemeBegin - 1;
                        return null;
                    } else {
                        state = 12;
                    }
                    break;
                }
                case 12: {
                    if (c == ASCII.SINGLE_QUOTE) { state = 13; }
                    else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                }
                case 13: {
                    Token token = new Token_Char(input.charAt(lexemeBegin + 1));
                    lexemeBegin = forward;
                    return token;
                }
            }
        }
    }

    private Token getString() {
        state = 14;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 14: {
                    if (c == ASCII.DOUBLE_QUOTE) {
                        state = 15;
                    } else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 15: {
                    if (c == ASCII.DOUBLE_QUOTE) {
                        state = 16;
                    }
                    break;
                }
                case 16: {
                    forward--;
                    Token token;
                    if (lexemeBegin + 1 == forward) {
                        token = new Token_String("");
                    } else {
                        token = new Token_String(input.substring(lexemeBegin + 1, forward));
                    }
                    lexemeBegin = forward;
                    return token;
                }
            }
        }
    }

    private Token getComment() {
        state = 17;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 17: {
                    if (c == ASCII.HASH) {
                        state = 18;
                    } else if (c == ASCII.OPEN_CURLY_BRACKET) {
                        state = 20;
                    } else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 18: {
                    if (c == ASCII.NEWLINE || c == ASCII.FORM_FEED) { state = 19; }
                    else { break; }
                }
                case 19: {
                    Token token = new Token_Comment(input.substring(lexemeBegin, forward));
                    lexemeBegin = forward;
                    return token;
                }
                case 20: {
                    if (c == ASCII.CLOSE_CURLY_BRACKET) { state = 21; }
                    else { break; }
                }
                case 21: {
                    Token token;
                    if (lexemeBegin + 1 == forward) {
                        token = new Token_Comment("");
                    } else {
                        token = new Token_Comment(input.substring(lexemeBegin + 1, forward));
                    }
                    lexemeBegin = forward + 1;
                    return token;
                }
            }
        }
    }

    private Token getColonOrSwapOrAssignment() {
        state = 22;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 22: {
                    if (c == ASCII.COLON) {
                        state = 23;
                    } else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 23: {
                    if (c == ASCII.EQUAL) {
                        state = 24;
                    } else {
                        forward--;
                        lexemeBegin = forward + 1;
                        return new Token_Colon();
                    }
                    break;
                }
                case 24: {
                    if (c == ASCII.COLON) {
                        lexemeBegin = forward;
                        return new Token_Swap();
                    } else {
                        forward--;
                        lexemeBegin = forward + 1;
                        return new Token_Assign();
                    }
                }
            }
        }

    }

    private Token getDotOrDoubleDot() {
        state = 25;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 25: {
                    if (c == ASCII.DOT) { state = 26; }
                    else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 26: {
                    if (c == ASCII.DOT) {
                        lexemeBegin = forward + 1;
                        return new Token_Double_Dot();
                    } else {
                        forward--;
                        lexemeBegin = forward + 1;
                        return new Token_Dot();
                    }
                }
            }
        }
    }

    private Token getLEorLTorNE() {
        state = 27;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 27: {
                    if (c == ASCII.LESS_THAN) { state = 28; }
                    else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 28: {
                    if (c == ASCII.EQUAL) {
                        lexemeBegin = forward + 1;
                        return new Token_LE();
                    }
                    else if (c == ASCII.GREATER_THAN) {
                        lexemeBegin = forward + 1;
                        return new Token_NE();
                    }
                    forward--;
                    lexemeBegin = forward + 1;
                    return new Token_LT();
                }

            }
        }
    }

    private Token getGEorGT() {
        state = 29;
        while (true) {
            int c = input.charAt(++forward);
            switch (state) {
                case 29: {
                    if (c == ASCII.GREATER_THAN) { state = 30; }
                    else {
                        forward = lexemeBegin - 1;
                        return null;
                    }
                    break;
                }
                case 30: {
                    if (c == ASCII.EQUAL) {
                        lexemeBegin = forward;
                        return new Token_GE();
                    } else {
                        forward--;
                        lexemeBegin = forward + 1;
                        return new Token_GT();
                    }
                }
            }
        }
    }

    private Token getEqual() {
        int c = input.charAt(++forward);
        if (c == ASCII.EQUAL) {
            lexemeBegin = forward + 1;
            return new Token_Equal();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getSemicolon() {
        int c = input.charAt(++forward);
        if (c == ASCII.SEMICOLON) {
            lexemeBegin = forward + 1;
            return new Token_Semicolon();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getComma() {
        int c = input.charAt(++forward);
        if (c == ASCII.COMMA) {
            lexemeBegin = forward + 1;
            return new Token_Comma();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getOpenBracket() {
        int c = input.charAt(++forward);
        if (c == ASCII.OPEN_BRACKET) {
            lexemeBegin = forward + 1;
            return new Token_Open_Bracket();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getCloseBracket() {
        int c = input.charAt(++forward);
        if (c == ASCII.CLOSE_BRACKET) {
            lexemeBegin = forward + 1;
            return new Token_Close_Bracket();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getPlus() {
        int c = input.charAt(++forward);
        if (c == ASCII.PLUS) {
            lexemeBegin = forward + 1;
            return new Token_Plus();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getMinus() {
        int c = input.charAt(++forward);
        if (c == ASCII.MINUS) {
            lexemeBegin = forward + 1;
            return new Token_Minus();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getMultiply() {
        int c = input.charAt(++forward);
        if (c == ASCII.MULTIPLY) {
            lexemeBegin = forward + 1;
            return new Token_Multiply();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getDivide() {
        int c = input.charAt(++forward);
        if (c == ASCII.DIVIDE) {
            lexemeBegin = forward + 1;
            return new Token_Divide();
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getReservedKeywordToken(String keyword) {
        switch (keyword) {
            case "program": {
                return new Token_Program();
            }
            case "var": {
                return new Token_Var();
            }
            case "const": {
                return new Token_Const();
            }
            case "type": {
                return new Token_Type();
            }
            case "function": {
                return new Token_Function();
            }
            case "return": {
                return new Token_Return();
            }
            case "begin": {
                return new Token_Begin();
            }
            case "end": {
                return new Token_End();
            }
            case "output": {
                return new Token_Output();
            }
            case "if": {
                return new Token_If();
            }
            case "then": {
                return new Token_Then();
            }
            case "else": {
                return new Token_Else();
            }
            case "while": {
                return new Token_While();
            }
            case "do": {
                return new Token_Do();
            }
            case "case": {
                return new Token_Case();
            }
            case "of": {
                return new Token_Of();
            }
            case "otherwise": {
                return new Token_Otherwise();
            }
            case "repeat": {
                return new Token_Repeat();
            }
            case "for": {
                return new Token_For();
            }
            case "until": {
                return new Token_Until();
            }
            case "loop": {
                return new Token_Loop();
            }
            case "pool": {
                return new Token_Pool();
            }
            case "exit": {
                return new Token_Exit();
            }
            case "mod": {
                return new Token_Mod();
            }
            case "and": {
                return new Token_And();
            }
            case "or": {
                return new Token_Or();
            }
            case "not": {
                return new Token_Not();
            }
            case "read": {
                return new Token_Read();
            }
            case "succ": {
                return new Token_Succ();
            }
            case "pred": {
                return new Token_Pred();
            }
            case "chr": {
                return new Token_Chr();
            }
            case "ord": {
                return new Token_Ord();
            }
            case "eof": {
                return new Token_EOF();
            }
            default:
                return null;
        }
    }

    private boolean isLetterOrUnderscore(int c) {
        return ((c >= ASCII.UPPER_A && c <= ASCII.UPPER_Z)
                || (c >= ASCII.LOWER_A && c <= ASCII.LOWER_Z)
                || (c == ASCII.UNDERSCORE));
    }

    private boolean isLetterOrDigitOrUnderscore(int c) {
        return ((c >= ASCII.UPPER_A && c <= ASCII.UPPER_Z)
                || (c >= ASCII.LOWER_A && c <= ASCII.LOWER_Z)
                || (c >= ASCII.ZERO && c <= ASCII.NINE)
                || (c == ASCII.UNDERSCORE));
    }

}


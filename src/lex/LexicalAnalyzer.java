package lex;

public class LexicalAnalyzer {

    private String input;
    private int lexemeBegin;
    private int forward;
    private int state;
    private final String[] reservedKeywords;

    public LexicalAnalyzer(String program) {
        lexemeBegin = 0;
        forward = -1;
        reservedKeywords = new String[]{"program", "var", "const", "type", "function", "return", "begin", "end",
                "output", "if", "then", "else", "while", "do", "case", "of", "otherwise", "repeat", "for", "until",
                "loop", "pool", "exit", "mod", "and", "or", "not", "read", "succ", "pred", "chr", "ord", "eof"
        };

        input = program;
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
            if (token != null && (token.type == TokenType.COMMENT || token.type == TokenType.WHITESPACE)) {
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

                    return new Token(TokenType.IDENTIFIER, identifier);
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
                    Token token = new Token(TokenType.INTEGER, input.substring(lexemeBegin, forward + 1));
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
                    Token token = new Token(TokenType.WHITESPACE, input.substring(lexemeBegin, forward + 1));
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
                    Token token = new Token(TokenType.CHAR, input.substring(lexemeBegin + 1, lexemeBegin + 2));
                    lexemeBegin = forward + 1;
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
                        token = new Token(TokenType.STRING, "");
                    } else {
                        token = new Token(TokenType.STRING, input.substring(lexemeBegin + 1, forward));
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
                    Token token = new Token(TokenType.COMMENT, input.substring(lexemeBegin, forward));
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
                        token = new Token(TokenType.COMMENT, "");
                    } else {
                        token = new Token(TokenType.COMMENT, input.substring(lexemeBegin + 1, forward));
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
                        return new Token(TokenType.COLON);
                    }
                    break;
                }
                case 24: {
                    if (c == ASCII.COLON) {
                        lexemeBegin = forward;
                        return new Token(TokenType.SWAP);
                    } else {
                        forward--;
                        lexemeBegin = forward + 1;
                        return new Token(TokenType.ASSIGN);
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
                        return new Token(TokenType.DOUBLE_DOT);
                    } else {
                        forward--;
                        lexemeBegin = forward + 1;
                        return new Token(TokenType.DOT);
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
                        return new Token(TokenType.LE);
                    }
                    else if (c == ASCII.GREATER_THAN) {
                        lexemeBegin = forward + 1;
                        return new Token(TokenType.NE);
                    }
                    forward--;
                    lexemeBegin = forward + 1;
                    return new Token(TokenType.LT);
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
                        lexemeBegin = forward + 1;
                        return new Token(TokenType.GE);
                    } else {
                        forward--;
                        lexemeBegin = forward + 1;
                        return new Token(TokenType.GT);
                    }
                }
            }
        }
    }

    private Token getEqual() {
        int c = input.charAt(++forward);
        if (c == ASCII.EQUAL) {
            lexemeBegin = forward + 1;
            return new Token(TokenType.EQUAL);
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
            return new Token(TokenType.SEMICOLON);
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
            return new Token(TokenType.COMMA);
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
            return new Token(TokenType.OPEN_BRACKET);
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
            return new Token(TokenType.CLOSE_BRACKET);
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
            return new Token(TokenType.PLUS);
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
            return new Token(TokenType.MINUS);
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
            return new Token(TokenType.MULTIPLY);
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
            return new Token(TokenType.DIVIDE);
        } else {
            forward--;
            lexemeBegin = forward + 1;
            return null;
        }
    }

    private Token getReservedKeywordToken(String keyword) {
        switch (keyword) {
            case "program": {
                return new Token(TokenType.PROGRAM);
            }
            case "var": {
                return new Token(TokenType.VAR);
            }
            case "const": {
                return new Token(TokenType.CONST);
            }
            case "type": {
                return new Token(TokenType.TYPE);
            }
            case "function": {
                return new Token(TokenType.FUNCTION);
            }
            case "return": {
                return new Token(TokenType.RETURN);
            }
            case "begin": {
                return new Token(TokenType.BEGIN);
            }
            case "end": {
                return new Token(TokenType.END);
            }
            case "output": {
                return new Token(TokenType.OUTPUT);
            }
            case "if": {
                return new Token(TokenType.IF);
            }
            case "then": {
                return new Token(TokenType.THEN);
            }
            case "else": {
                return new Token(TokenType.ELSE);
            }
            case "while": {
                return new Token(TokenType.WHILE);
            }
            case "do": {
                return new Token(TokenType.DO);
            }
            case "case": {
                return new Token(TokenType.CASE);
            }
            case "of": {
                return new Token(TokenType.OF);
            }
            case "otherwise": {
                return new Token(TokenType.OTHERWISE);
            }
            case "repeat": {
                return new Token(TokenType.REPEAT);
            }
            case "for": {
                return new Token(TokenType.FOR);
            }
            case "until": {
                return new Token(TokenType.UNTIL);
            }
            case "loop": {
                return new Token(TokenType.LOOP);
            }
            case "pool": {
                return new Token(TokenType.POOL);
            }
            case "exit": {
                return new Token(TokenType.EXIT);
            }
            case "mod": {
                return new Token(TokenType.MOD);
            }
            case "and": {
                return new Token(TokenType.AND);
            }
            case "or": {
                return new Token(TokenType.OR);
            }
            case "not": {
                return new Token(TokenType.NOT);
            }
            case "read": {
                return new Token(TokenType.READ);
            }
            case "succ": {
                return new Token(TokenType.SUCC);
            }
            case "pred": {
                return new Token(TokenType.PRED);
            }
            case "chr": {
                return new Token(TokenType.CHR);
            }
            case "ord": {
                return new Token(TokenType.ORD);
            }
            case "eof": {
                return new Token(TokenType.EOF);
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


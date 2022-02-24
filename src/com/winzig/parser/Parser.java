package com.winzig.parser;

import com.winzig.lexical.analyzer.*;

import java.util.ArrayList;

public class Parser {

    private LexicalAnalyzer lex;
    private ArrayList<Token> stack;

    public Parser(LexicalAnalyzer lex) {
        this.lex = lex;
        this.stack = new ArrayList<>();
    }

    public void parse() {
        Winzig();
    }

    private void Winzig() {
        Token program = lex.getNextToken();
        assert program instanceof Token_Program;

        Name();

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;

        Consts();
        Types();
        Dclns();
        Subprogs();
        Body();
        Name();
        
        Token dot = lex.getNextToken();
        assert dot instanceof Token_Dot;

        buildTree(program, 7);
    }

    private void Consts() {
        Token _const = lex.readNextToken();

        if (_const instanceof Token_Const) {
            lex.getNextToken();

            int numConst = 0;
            Const();
            numConst++;

            while (lex.readNextToken() instanceof Token_Comma) {
                lex.getNextToken();
                Const();
                numConst++;
            }

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;

            buildTree(new Token_Consts(), numConst);
        } else {
            buildTree(new Token_Consts(), 0);
        }
    }

    private void Const() {
        Name();

        Token equal = lex.getNextToken();
        assert equal instanceof Token_Equal;

        ConstValue();

        buildTree(new Token_Const(), 2);
    }

    private void ConstValue() {
        Token nextToken = lex.getNextToken();
        if (nextToken instanceof Token_Integer || nextToken instanceof Token_Char) {
            buildTree(nextToken, 0);
        } else {
            Name();
        }
    }

    private void Types() {
        Token type = lex.readNextToken();

        if (type instanceof Token_Type) {
            lex.getNextToken();

            int numType = 0;
            Type();
            numType++;

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;

            while (lex.readNextToken() instanceof Token_Identifier) {
                Type();
                semicolon = lex.getNextToken();
                assert semicolon instanceof Token_Semicolon;
                numType++;
            }

            buildTree(new Token_Types(), numType);

        } else {
            buildTree(new Token_Types(), 0);
        }
    }

    private void Type() {
        Name();

        Token equal = lex.getNextToken();
        assert equal instanceof Token_Equal;

        LitList();

        buildTree(new Token_Type(), 2);
    }

    private void LitList() {
        Token openBracket = lex.getNextToken();
        assert openBracket instanceof Token_Open_Bracket;

        int numName = 0;
        Name();
        numName++;

        while (lex.readNextToken() instanceof Token_Comma) {
            lex.getNextToken();
            Name();
            numName++;
        }

        Token closeBracket = lex.getNextToken();
        assert closeBracket instanceof Token_Close_Bracket;

        buildTree(new Token_Lit(), numName);
    }

    private void Subprogs() {
        int numFcn = 0;
        while (lex.readNextToken() instanceof Token_Function) {
            Fcn();
            numFcn++;
        }
        buildTree(new Token_Subprogs(), numFcn);
    }

    private void Fcn() {
        Token function = lex.getNextToken();
        assert function instanceof Token_Function;

        Name();

        Token openBracket = lex.getNextToken();
        assert openBracket instanceof Token_Open_Bracket;

        Params();

        Token closeBracket = lex.getNextToken();
        assert closeBracket instanceof Token_Close_Bracket;

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;

        Name();

        Token semicolon = lex.getNextToken();
        assert semicolon instanceof Token_Semicolon;

        Consts(); Types(); Dclns(); Body(); Name();

        semicolon = lex.getNextToken();
        assert semicolon instanceof Token_Semicolon;

        buildTree(new Token_Fcn(), 8);
    }

    private void Params() {
        int numDcln = 0;
        Dcln();
        numDcln++;

        while (lex.readNextToken() instanceof Token_Semicolon) {
            lex.getNextToken();
            Dcln();
            numDcln++;
        }

        buildTree(new Token_Params(), numDcln);
    }

    private void Dclns() {
        Token _var = lex.readNextToken();

        if (_var instanceof Token_Var) {
            lex.getNextToken();

            int numDcln = 0;
            Dcln();
            numDcln++;

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;

            while (lex.readNextToken() instanceof Token_Identifier){
                Dcln();
                semicolon = lex.getNextToken();
                assert semicolon instanceof Token_Semicolon;
                numDcln++;
            }

            buildTree(new Token_Dclns(), numDcln);

        } else {
            buildTree(new Token_Dclns(), 0);
        }
    }

    private void Dcln() {
        int numName = 0;
        Name();
        numName++;

        while (lex.readNextToken() instanceof Token_Comma) {
            lex.getNextToken();
            Name();
            numName++;
        }

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;

        Name();
        numName++;

        buildTree(new Token_Var(), numName);
    }

    private void Body() {
        Token begin = lex.getNextToken();
        assert begin instanceof Token_Begin;

        int numStmt = 0;
        Statement();
        numStmt++;

        while (lex.readNextToken() instanceof Token_Semicolon) {
            lex.getNextToken();
            Statement();
            numStmt++;
        }

        Token end = lex.getNextToken();
        assert end instanceof Token_End;

        buildTree(new Token_Block(), numStmt);
    }

    private void Statement() {
        Token nextToken = lex.readNextToken();

        if (nextToken instanceof Token_Identifier) {
            Assignment();

        } else if (nextToken instanceof Token_Output) {
            Token output = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;

            int numOutExp = 0;
            OutExp();
            numOutExp++;

            while (lex.readNextToken() instanceof Token_Comma) {
                lex.getNextToken();
                OutExp();
                numOutExp++;
            }

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

            buildTree(output, numOutExp);

        } else if (nextToken instanceof  Token_If) {
            Token _if = lex.getNextToken();

            Expression();

            Token then = lex.getNextToken();
            assert then instanceof Token_Then;

            int numStmt = 0;
            Statement();
            numStmt++;

            if (lex.readNextToken() instanceof Token_Else) {
                lex.getNextToken();
                Statement();
                numStmt++;
            }

            buildTree(_if, numStmt + 1);

        } else if (nextToken instanceof Token_While) {
            Token _while = lex.getNextToken();

            Token _do = lex.getNextToken();
            assert _do instanceof Token_Do;

            Statement();

            buildTree(_while, 2);

        } else if (nextToken instanceof Token_Repeat) {
            Token repeat = lex.getNextToken();

            int numStmt = 0;
            Statement();
            numStmt++;

            while (lex.readNextToken() instanceof Token_Semicolon) {
                lex.getNextToken();
                Statement();
                numStmt++;
            }

            Token until = lex.getNextToken();
            assert until instanceof Token_Until;

            Expression();

            buildTree(repeat, numStmt + 1);
        } else if (nextToken instanceof Token_For) {
            Token _for = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;

            ForStat();

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;

            ForExp();

            semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;

            ForStat();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

            Statement();

            buildTree(_for, 4);

        } else if (nextToken instanceof Token_Loop) {
            Token loop = lex.getNextToken();

            int numStmt = 0;
            Statement();
            numStmt++;

            while (lex.readNextToken() instanceof Token_Semicolon) {
                lex.getNextToken();
                Statement();
                numStmt++;
            }

            Token pool = lex.getNextToken();
            assert pool instanceof Token_Pool;

            buildTree(loop, numStmt);

        } else if (nextToken instanceof Token_Case) {
            Token _case = lex.getNextToken();

            Expression();

            Token of = lex.getNextToken();
            assert of instanceof Token_Of;

            Caseclauses();

            OtherwiseClause();

            Token end = lex.getNextToken();
            assert end instanceof Token_End;

            buildTree(_case, 3);

        } else if (nextToken instanceof Token_Read) {
            Token read = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;

            int numName = 0;
            Name();
            numName++;

            while (lex.readNextToken() instanceof Token_Comma) {
                lex.getNextToken();
                Name();
                numName++;
            }

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

            buildTree(read, numName);

        } else if (nextToken instanceof Token_Exit) {
            Token exit = lex.getNextToken();
            buildTree(exit, 0);

        } else if (nextToken instanceof Token_Return) {
            Token _return = lex.getNextToken();
            Expression();
            buildTree(_return, 1);

        } else {
            buildTree(new Token_Null(), 0);
        }
    }

    private void OutExp() {
        Token token = lex.readNextToken();

        if (token instanceof Token_String) {
            StringNode();
            buildTree(new Token_String(((Token_String) token).getValue()), 1);

        } else {
            boolean isPrimary = (token instanceof Token_Minus) || (token instanceof Token_Plus)
                    || (token instanceof Token_Not) || (token instanceof Token_EOF)
                    || (token instanceof Token_Integer) || (token instanceof Token_Char)
                    || (token instanceof Token_Open_Bracket) || (token instanceof Token_Succ)
                    || (token instanceof Token_Pred) || (token instanceof Token_Chr)
                    || (token instanceof Token_Ord);

            if (isPrimary) {
                Expression();
                buildTree(new Token_Integer(), 0);
            } else {
                // error
            }
        }
    }

    private void StringNode() {
        Token string = lex.getNextToken();
        assert string instanceof Token_String;
    }

    private void Caseclauses() {
        CaseClause();

        Token semicolon = lex.getNextToken();
        assert semicolon instanceof Token_Semicolon;

        Token nextToken = lex.readNextToken();
        boolean hasCaseClause = nextToken instanceof Token_Integer || nextToken instanceof Token_Char || nextToken instanceof Token_Identifier;
        while (hasCaseClause) {

            CaseClause();

            semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;

            nextToken = lex.readNextToken();
            hasCaseClause = nextToken instanceof Token_Integer || nextToken instanceof Token_Char || nextToken instanceof Token_Identifier;
        }
    }

    private void CaseClause() {
        int numCaseExpression = 0;
        CaseExpression();
        numCaseExpression++;

        while (lex.readNextToken() instanceof Token_Comma) {
            lex.getNextToken();
            CaseExpression();
            numCaseExpression++;
        }

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;

        Statement();

        buildTree(new Token_Case_Clause(), numCaseExpression + 1);
    }

    private void CaseExpression() {
        ConstValue();

        if (lex.readNextToken() instanceof Token_Double_Dot) {
            Token dd = lex.getNextToken();
            ConstValue();
            buildTree(dd, 2);
        }
    }

    private void OtherwiseClause() {
        if (lex.readNextToken() instanceof Token_Otherwise) {
            Token otherwise = lex.getNextToken();
            Statement();
            buildTree(otherwise, 1);
        }
    }

    private void Assignment() {
        Name();

        if (lex.readNextToken() instanceof Token_Assign) {
            Token assign = lex.getNextToken();
            Expression();
            buildTree(assign, 2);
        } else if (lex.readNextToken() instanceof  Token_Swap) {
            Token swap = lex.getNextToken();
            Name();
            buildTree(swap, 2);
        } else {
            // error
        }
    }

    private void ForStat() {
        if (lex.readNextToken() instanceof Token_Identifier) {
            Assignment();
        } else {
            buildTree(new Token_Null(), 0);
        }
    }

    private void ForExp() {
        Token token = lex.readNextToken();
        boolean isPrimary = (token instanceof Token_Minus) || (token instanceof Token_Plus)
                || (token instanceof Token_Not) || (token instanceof Token_EOF)
                || (token instanceof Token_Integer) || (token instanceof Token_Char)
                || (token instanceof Token_Open_Bracket) || (token instanceof Token_Succ)
                || (token instanceof Token_Pred) || (token instanceof Token_Chr)
                || (token instanceof Token_Ord);
        if (isPrimary) {
            Expression();
        } else {
            buildTree(new Token_True(), 0);
        }
    }

    private void Expression() {
        Term();

        Token nextToken = lex.readNextToken();

        if (nextToken instanceof Token_LE) {
            Token le = lex.getNextToken();
            Term();
            buildTree(le, 2);

        } else if (nextToken instanceof Token_LT) {
            Token lt = lex.getNextToken();
            Term();
            buildTree(lt, 2);

        } else if (nextToken instanceof Token_GE) {
            Token ge = lex.getNextToken();
            Term();
            buildTree(ge, 2);

        } else if (nextToken instanceof Token_GT) {
            Token gt = lex.getNextToken();
            Term();
            buildTree(gt, 2);

        } else if (nextToken instanceof Token_Equal) {
            Token equal = lex.getNextToken();
            Term();
            buildTree(equal, 2);

        } else if (nextToken instanceof  Token_NE) {
            Token not_equal = lex.getNextToken();
            Term();
            buildTree(not_equal, 2);

        }
    }

    private void Term() {
        Factor();

        Token nextToken = lex.readNextToken();

        while (nextToken instanceof Token_Plus) {
            Token plus = lex.getNextToken();
            Factor();
            buildTree(plus, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Minus) {
            Token minus = lex.getNextToken();
            Factor();
            buildTree(minus, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Or) {
            Token or = lex.getNextToken();
            Factor();
            buildTree(or, 2);
            nextToken = lex.readNextToken();
        }
    }

    private void Factor() {
        Primary();

        Token nextToken = lex.readNextToken();

        while (nextToken instanceof Token_Multiply) {
            Token multiply = lex.getNextToken();
            Primary();
            buildTree(multiply, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Divide) {
            Token divide = lex.getNextToken();
            Primary();
            buildTree(divide, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_And) {
            Token and = lex.getNextToken();
            Primary();
            buildTree(and, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Mod) {
            Token mod = lex.getNextToken();
            Primary();
            buildTree(mod, 2);
            nextToken = lex.readNextToken();
        }
    }

    private void Primary() {
        Token nextToken = lex.readNextToken();

        if (nextToken instanceof Token_Minus) {
            Token minus = lex.getNextToken();
            Primary();
            buildTree(minus, 1);

        } else if (nextToken instanceof Token_Plus) {
            Token plus = lex.getNextToken();
            Primary();
            buildTree(plus, 1);

        } else if (nextToken instanceof Token_Not) {
            Token not = lex.getNextToken();
            Primary();
            buildTree(not, 1);

        } else if (nextToken instanceof Token_EOF) {
            Token eof = lex.getNextToken();
            buildTree(eof, 0);

        } else if (nextToken instanceof Token_Integer) {
            Token integer = lex.getNextToken();
            buildTree(integer, 0);

        } else if (nextToken instanceof Token_Char) {
            Token _char = lex.getNextToken();
            buildTree(_char, 0);

        } else if (nextToken instanceof Token_Open_Bracket) {
            lex.getNextToken();
            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

        } else if (nextToken instanceof Token_Succ) {
            Token succ = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

            buildTree(succ, 1);

        } else if (nextToken instanceof Token_Pred) {
            Token pred = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

            buildTree(pred, 1);

        } else if (nextToken instanceof Token_Chr) {
            Token chr = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

            buildTree(chr, 1);

        } else if (nextToken instanceof Token_Ord) {
            Token ord = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;

            buildTree(ord, 1);

        }
    }

    private void Name() {
        Token id = lex.getNextToken();
        assert id instanceof Token_Identifier;

        buildTree(id, 0);
    }


    private void buildTree(Token token, int numChildren) {

    }
}

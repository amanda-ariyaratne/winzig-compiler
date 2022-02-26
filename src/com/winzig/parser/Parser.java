package com.winzig.parser;

import com.winzig.lexical.analyzer.*;

import java.util.ArrayList;
import java.util.Collections;

public class Parser {

    private LexicalAnalyzer lex;
    private ArrayList<Node> stack;

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
//        System.out.println(program);

        Name();

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;
//        System.out.println(colon);

        Consts();
        Types();
        Dclns();
        Subprogs();
        Body();
        Name();
        
        Token dot = lex.getNextToken();
        assert dot instanceof Token_Dot;
//        System.out.println(dot);

        System.out.println("program parsing finished");
        buildTree(program, 7);
    }

    private void Consts() {
        Token _const = lex.readNextToken();

        if (_const instanceof Token_Const) {
            lex.getNextToken();
//            System.out.println(_const);
            int numConst = 0;
            Const();
            numConst++;

            while (lex.readNextToken() instanceof Token_Comma) {
                lex.getNextToken(); // System.out.println(lex.getNextToken());
                Const();
                numConst++;
            }

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;
//            System.out.println(semicolon);
            buildTree(new Token_Consts(), numConst);
        } else {
            buildTree(new Token_Consts(), 0);
        }
    }

    private void Const() {
        Name();

        Token equal = lex.getNextToken();
        assert equal instanceof Token_Equal;
//        System.out.println(equal);
        ConstValue();

        buildTree(new Token_Const(), 2);
    }

    private void ConstValue() {
        Token nextToken = lex.readNextToken();
        if (nextToken instanceof Token_Integer || nextToken instanceof Token_Char) {
            lex.getNextToken();
//            System.out.println(nextToken);
            buildTree(nextToken, 0);
        } else {
            Name();
        }
    }

    private void Types() {
        Token type = lex.readNextToken();

        if (type instanceof Token_Type) {
            lex.getNextToken();
//            System.out.println(type);
            int numType = 0;
            Type();
            numType++;

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;
//            System.out.println(semicolon);
            while (lex.readNextToken() instanceof Token_Identifier) {
                Type();
                semicolon = lex.getNextToken();
                assert semicolon instanceof Token_Semicolon;
//                System.out.println(semicolon);
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
//        System.out.println(equal);
        LitList();

        buildTree(new Token_Type(), 2);
    }

    private void LitList() {
        Token openBracket = lex.getNextToken();
        assert openBracket instanceof Token_Open_Bracket;
//        System.out.println(openBracket);
        int numName = 0;
        Name();
        numName++;

        while (lex.readNextToken() instanceof Token_Comma) {
            lex.getNextToken(); // System.out.println(lex.getNextToken());
            Name();
            numName++;
        }

        Token closeBracket = lex.getNextToken();
        assert closeBracket instanceof Token_Close_Bracket;
//        System.out.println(closeBracket);
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
//        System.out.println(function);
        Name();

        Token openBracket = lex.getNextToken();
        assert openBracket instanceof Token_Open_Bracket;
//        System.out.println(openBracket);
        Params();

        Token closeBracket = lex.getNextToken();
        assert closeBracket instanceof Token_Close_Bracket;
//        System.out.println(closeBracket);

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;
//        System.out.println(colon);

        Name();

        Token semicolon = lex.getNextToken();
        assert semicolon instanceof Token_Semicolon;
//        System.out.println(semicolon);

        Consts(); Types(); Dclns(); Body(); Name();

        semicolon = lex.getNextToken();
        assert semicolon instanceof Token_Semicolon;
//        System.out.println(semicolon);
        buildTree(new Token_Fcn(), 8);
    }

    private void Params() {
        int numDcln = 0;
        Dcln();
        numDcln++;

        while (lex.readNextToken() instanceof Token_Semicolon) {
            lex.getNextToken(); // System.out.println(lex.getNextToken());
            Dcln();
            numDcln++;
        }

        buildTree(new Token_Params(), numDcln);
    }

    private void Dclns() {
        Token _var = lex.readNextToken();

        if (_var instanceof Token_Var) {
            lex.getNextToken();
//            System.out.println(_var);

            int numDcln = 0;
            Dcln();
            numDcln++;

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;
//            System.out.println(semicolon);

            while (lex.readNextToken() instanceof Token_Identifier){
                Dcln();
                semicolon = lex.getNextToken();
                assert semicolon instanceof Token_Semicolon;
//                System.out.println(semicolon);
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
            lex.getNextToken(); // System.out.println(lex.getNextToken());
            Name();
            numName++;
        }

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;
//        System.out.println(colon);

        Name();
        numName++;

        buildTree(new Token_Var(), numName);
    }

    private void Body() {
        Token begin = lex.getNextToken();
        assert begin instanceof Token_Begin;
//        System.out.println(begin);
        int numStmt = 0;
        Statement();
        numStmt++;

        while (lex.readNextToken() instanceof Token_Semicolon) {
            lex.getNextToken(); // System.out.println(lex.getNextToken());
            Statement();
            numStmt++;
        }

        Token end = lex.getNextToken();
        assert end instanceof Token_End;
//        System.out.println(end);

        buildTree(new Token_Block(), numStmt);
    }

    private void Statement() {
        Token nextToken = lex.readNextToken();

        if (nextToken instanceof Token_Identifier) {
            Assignment();

        } else if (nextToken instanceof Token_Output) {
            Token output = lex.getNextToken();
//            System.out.println(output);
            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;
//            System.out.println(openBracket);
            int numOutExp = 0;
            OutExp();
            numOutExp++;

            while (lex.readNextToken() instanceof Token_Comma) {
                lex.getNextToken(); // System.out.println(lex.getNextToken());
                OutExp();
                numOutExp++;
            }

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);
            buildTree(output, numOutExp);

        } else if (nextToken instanceof  Token_If) {
            Token _if = lex.getNextToken();
//            System.out.println(_if);
            Expression();

            Token then = lex.getNextToken();
            assert then instanceof Token_Then;
//            System.out.println(then);
            int numStmt = 0;
            Statement();
            numStmt++;

            if (lex.readNextToken() instanceof Token_Else) {
                lex.getNextToken(); // System.out.println(lex.getNextToken());
                Statement();
                numStmt++;
            }

            buildTree(_if, numStmt + 1);

        } else if (nextToken instanceof Token_While) {
            Token _while = lex.getNextToken();

            Token _do = lex.getNextToken();
            assert _do instanceof Token_Do;
//            System.out.println(_do);

            Statement();

            buildTree(_while, 2);

        } else if (nextToken instanceof Token_Repeat) {
            Token repeat = lex.getNextToken();
//            System.out.println(repeat);
            int numStmt = 0;
            Statement();
            numStmt++;

            while (lex.readNextToken() instanceof Token_Semicolon) {
                lex.getNextToken(); // System.out.println(lex.getNextToken());
                Statement();
                numStmt++;
            }

            Token until = lex.getNextToken();
            assert until instanceof Token_Until;
//            System.out.println(until);

            Expression();

            buildTree(repeat, numStmt + 1);
        } else if (nextToken instanceof Token_For) {
            Token _for = lex.getNextToken();
//            System.out.println(_for);
            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;
//            System.out.println(openBracket);
            ForStat();

            Token semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;
//            System.out.println(semicolon);
            ForExp();

            semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;
//            System.out.println(semicolon);
            ForStat();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);
            Statement();

            buildTree(_for, 4);

        } else if (nextToken instanceof Token_Loop) {
            Token loop = lex.getNextToken();
//            System.out.println(loop);
            int numStmt = 0;
            Statement();
            numStmt++;

            while (lex.readNextToken() instanceof Token_Semicolon) {
                lex.getNextToken(); // System.out.println(lex.getNextToken());
                Statement();
                numStmt++;
            }

            Token pool = lex.getNextToken();
            assert pool instanceof Token_Pool;
//            System.out.println(pool);
            buildTree(loop, numStmt);

        } else if (nextToken instanceof Token_Case) {
            Token _case = lex.getNextToken();
//            System.out.println(_case);
            Expression();

            Token of = lex.getNextToken();
            assert of instanceof Token_Of;
//            System.out.println(of);
            Caseclauses();

            OtherwiseClause();

            Token end = lex.getNextToken();
            assert end instanceof Token_End;
//            System.out.println(end);
            buildTree(_case, 3);

        } else if (nextToken instanceof Token_Read) {
            Token read = lex.getNextToken();
//            System.out.println(read);

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;
//            System.out.println(openBracket);
            int numName = 0;
            Name();
            numName++;

            while (lex.readNextToken() instanceof Token_Comma) {
                lex.getNextToken(); // System.out.println(lex.getNextToken());
                Name();
                numName++;
            }

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);

            buildTree(read, numName);

        } else if (nextToken instanceof Token_Exit) {
            Token exit = lex.getNextToken();
//            System.out.println(exit);
            buildTree(exit, 0);

        } else if (nextToken instanceof Token_Return) {
            Token _return = lex.getNextToken();
//            System.out.println(_return);
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
            Token_String strToken = (Token_String) token;
            buildTree(new Token_String(strToken.value), 1);

        } else {
            boolean isPrimary = (token instanceof Token_Minus) || (token instanceof Token_Plus)
                    || (token instanceof Token_Not) || (token instanceof Token_EOF)
                    || (token instanceof Token_Integer) || (token instanceof Token_Char)
                    || (token instanceof Token_Open_Bracket) || (token instanceof Token_Succ)
                    || (token instanceof Token_Pred) || (token instanceof Token_Chr)
                    || (token instanceof Token_Ord) || (token instanceof Token_Identifier);

            if (isPrimary) {
                Expression();
                buildTree(new Token_Output_Integer(), 1);
            } else {
                // error
            }
        }
    }

    private void StringNode() {
        Token string = lex.getNextToken();
        assert string instanceof Token_String;
//        System.out.println(string);
    }

    private void Caseclauses() {
        CaseClause();

        Token semicolon = lex.getNextToken();
        assert semicolon instanceof Token_Semicolon;
//        System.out.println(semicolon);

        Token nextToken = lex.readNextToken();
        boolean hasCaseClause = nextToken instanceof Token_Integer || nextToken instanceof Token_Char || nextToken instanceof Token_Identifier;
        while (hasCaseClause) {

            CaseClause();

            semicolon = lex.getNextToken();
            assert semicolon instanceof Token_Semicolon;
//            System.out.println(semicolon);

            nextToken = lex.readNextToken();
            hasCaseClause = nextToken instanceof Token_Integer || nextToken instanceof Token_Char || nextToken instanceof Token_Identifier;
        }
    }

    private void CaseClause() {
        int numCaseExpression = 0;
        CaseExpression();
        numCaseExpression++;

        while (lex.readNextToken() instanceof Token_Comma) {
            lex.getNextToken(); // System.out.println(lex.getNextToken());
            CaseExpression();
            numCaseExpression++;
        }

        Token colon = lex.getNextToken();
        assert colon instanceof Token_Colon;
//        System.out.println(colon);
        Statement();

        buildTree(new Token_Case_Clause(), numCaseExpression + 1);
    }

    private void CaseExpression() {
        ConstValue();

        if (lex.readNextToken() instanceof Token_Double_Dot) {
            Token dd = lex.getNextToken();
//            System.out.println(dd);
            ConstValue();
            buildTree(dd, 2);
        }
    }

    private void OtherwiseClause() {
        if (lex.readNextToken() instanceof Token_Otherwise) {
            Token otherwise = lex.getNextToken();
//            System.out.println(otherwise);
            Statement();
            buildTree(otherwise, 1);
        }
    }

    private void Assignment() {
        Name();

        if (lex.readNextToken() instanceof Token_Assign) {
            Token assign = lex.getNextToken();
//            System.out.println(assign);
            Expression();
            buildTree(assign, 2);
        } else if (lex.readNextToken() instanceof  Token_Swap) {
            Token swap = lex.getNextToken();
//            System.out.println(swap);
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
                || (token instanceof Token_Ord) || (token instanceof Token_Identifier);
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
//            System.out.println(le);
            Term();
            buildTree(le, 2);

        } else if (nextToken instanceof Token_LT) {
            Token lt = lex.getNextToken();
//            System.out.println(lt);
            Term();
            buildTree(lt, 2);

        } else if (nextToken instanceof Token_GE) {
            Token ge = lex.getNextToken();
//            System.out.println(ge);
            Term();
            buildTree(ge, 2);

        } else if (nextToken instanceof Token_GT) {
            Token gt = lex.getNextToken();
//            System.out.println(gt);
            Term();
            buildTree(gt, 2);

        } else if (nextToken instanceof Token_Equal) {
            Token equal = lex.getNextToken();
//            System.out.println(equal);
            Term();
            buildTree(equal, 2);

        } else if (nextToken instanceof  Token_NE) {
            Token not_equal = lex.getNextToken();
//            System.out.println(not_equal);
            Term();
            buildTree(not_equal, 2);

        }
    }

    private void Term() {
        Factor();

        Token nextToken = lex.readNextToken();

        while (nextToken instanceof Token_Plus) {
            Token plus = lex.getNextToken();
//            System.out.println(plus);
            Factor();
            buildTree(plus, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Minus) {
            Token minus = lex.getNextToken();
//            System.out.println(minus);
            Factor();
            buildTree(minus, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Or) {
            Token or = lex.getNextToken();
//            System.out.println(or);
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
//            System.out.println(multiply);
            Primary();
            buildTree(multiply, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Divide) {
            Token divide = lex.getNextToken();
//            System.out.println(divide);
            Primary();
            buildTree(divide, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_And) {
            Token and = lex.getNextToken();
//            System.out.println(and);
            Primary();
            buildTree(and, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken instanceof Token_Mod) {
            Token mod = lex.getNextToken();
//            System.out.println(mod);
            Primary();
            buildTree(mod, 2);
            nextToken = lex.readNextToken();
        }
    }

    private void Primary() {
        Token nextToken = lex.readNextToken();

        if (nextToken instanceof Token_Minus) {
            Token minus = lex.getNextToken();
//            System.out.println(minus);
            Primary();
            buildTree(minus, 1);

        } else if (nextToken instanceof Token_Plus) {
            Token plus = lex.getNextToken();
//            System.out.println(plus);
            Primary();
            buildTree(plus, 1);

        } else if (nextToken instanceof Token_Not) {
            Token not = lex.getNextToken();
//            System.out.println(not);
            Primary();
            buildTree(not, 1);

        } else if (nextToken instanceof Token_EOF) {
            Token eof = lex.getNextToken();
//            System.out.println(eof);
            buildTree(eof, 0);

        } else if (nextToken instanceof Token_Integer) {
            Token integer = lex.getNextToken();
//            System.out.println(integer);
            buildTree(integer, 0);

        } else if (nextToken instanceof Token_Char) {
            Token _char = lex.getNextToken();
//            System.out.println(_char);
            buildTree(_char, 0);

        } else if (nextToken instanceof Token_Open_Bracket) {
//            System.out.println(nextToken);
            lex.getNextToken();
            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);

        } else if (nextToken instanceof Token_Succ) {
            Token succ = lex.getNextToken();
//            System.out.println(succ);

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;
//            System.out.println(openBracket);

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);

            buildTree(succ, 1);

        } else if (nextToken instanceof Token_Pred) {
            Token pred = lex.getNextToken();
//            System.out.println(pred);

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;
//            System.out.println(openBracket);

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);

            buildTree(pred, 1);

        } else if (nextToken instanceof Token_Chr) {
            Token chr = lex.getNextToken();
//            System.out.println(chr);

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;
//            System.out.println(openBracket);

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);

            buildTree(chr, 1);

        } else if (nextToken instanceof Token_Ord) {
            Token ord = lex.getNextToken();
//            System.out.println(ord);

            Token openBracket = lex.getNextToken();
            assert openBracket instanceof Token_Open_Bracket;
//            System.out.println(openBracket);

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket instanceof Token_Close_Bracket;
//            System.out.println(closeBracket);

            buildTree(ord, 1);

        } else if (nextToken instanceof Token_Identifier) {

            Name();

            Token followingToken = lex.readNextToken();
            if (followingToken instanceof Token_Open_Bracket) {
                Token openBracket = lex.getNextToken();
//                System.out.println(openBracket);
                int numExpr = 0;
                Expression();
                numExpr++;

                while (lex.readNextToken() instanceof Token_Comma) {
                    lex.getNextToken(); // System.out.println(lex.getNextToken());
                    Expression();
                    numExpr++;
                }

                Token closeBracket = lex.getNextToken();
                assert closeBracket instanceof Token_Close_Bracket;
//                System.out.println(closeBracket);

                buildTree(new Token_Call(), numExpr + 1);
            }

        }
    }

    private void Name() {
        Token id = lex.getNextToken();
        assert id instanceof Token_Identifier;
//        System.out.println(id);
        buildTree(id, 0);
    }


    private void buildTree(Token token, int numChildren) {
        Node node = new Node(token, numChildren);
        if (numChildren == 0) {
            stack.add(node);
        } else if (numChildren == 1) {
            Node child = stack.remove(stack.size() - 1);
            node.firstChild = child;
            stack.add(node);
        } else {
            Node rightSibling = stack.remove(stack.size() - 1);
            for (int i = numChildren - 1; i > 0; i--) {
                Node leftSibling = stack.remove(stack.size() - 1);
                leftSibling.nextSibling = rightSibling;
                rightSibling = leftSibling;
            }
            node.firstChild = rightSibling;
            stack.add(node);
        }
        // traverseTree();
        // System.out.println("\n");
    }

    public void traverseTree() {
        Node root = stack.get(stack.size() - 1);
        traverseTreeRecurse(root, 0);
    }

    private void traverseTreeRecurse(Node root, int depth) {
        if (root.token instanceof Token_Identifier) {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) + "<identifier>(1)");
            System.out.println(String.join("", Collections.nCopies(depth + 1, ". "))
                    + ((Token_Identifier) root.token).value + "(0)");
        } else if (root.token instanceof Token_Integer) {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) + "<integer>(1)");
            System.out.println(String.join("", Collections.nCopies(depth + 1, ". "))
                    + ((Token_Integer) root.token).value + "(0)");
        } else {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) +
                    root.token.getName() + "(" + root.noOfChildren + ")");
        }
        if (root.firstChild != null) {
            traverseTreeRecurse(root.firstChild, depth + 1);
        }
        if (root.nextSibling != null) {
            traverseTreeRecurse(root.nextSibling, depth);
        }
    }
}

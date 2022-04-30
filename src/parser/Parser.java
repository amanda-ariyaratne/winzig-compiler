package parser;

import lex.LexicalAnalyzer;
import lex.Token;
import lex.TokenType;

import java.util.ArrayList;

public class Parser {

    private LexicalAnalyzer lex;
    private ArrayList<Node> stack;

    public Parser(LexicalAnalyzer lex) {
        this.lex = lex;
        this.stack = new ArrayList<>();
    }

    public Node parse() {
        Winzig();
        return stack.get(stack.size() - 1);
    }

    /**
     *  Winzig  --> 'program' Name ':' Consts Types Dclns Subprogs Body Name '.' ==> 'program'
     *    P     --> < 'program' <identifier> E E E E E <identifier> >
     */
    private void Winzig() {
        Token program = lex.getNextToken();
        assert program.type == TokenType.PROGRAM;

        Name();

        Token colon = lex.getNextToken();
        assert colon.type == TokenType.COLON;

        Consts();
        Types();
        Dclns();
        Subprogs();
        Body();
        Name();
        
        Token dot = lex.getNextToken();
        assert dot.type == TokenType.DOT;

        buildTree(program, 7);
    }

    /**
     *  Consts  --> 'const' Const list ',' ';'   ==> 'consts'
     *          -->                              ==> 'consts'
     *    E     --> < 'consts' E+ >
     *          --> < 'consts' >
     */
    private void Consts() {
        Token _const = lex.readNextToken();

        if (_const.type == TokenType.CONST) {
            lex.getNextToken();
            int numConst = 0;
            Const();
            numConst++;

            while (lex.readNextToken().type == TokenType.COMMA) {
                lex.getNextToken();
                Const();
                numConst++;
            }

            Token semicolon = lex.getNextToken();
            assert semicolon.type == TokenType.SEMICOLON;
            buildTree(new Token(TokenType.CONSTS), numConst);
        } else {
            buildTree(new Token(TokenType.CONSTS), 0);
        }
    }

    /**
     *  Const   --> Name '=' ConstValue       ==> 'const'
     *    E     --> < 'const' <identifier> E >
     */
    private void Const() {
        Name();

        Token equal = lex.getNextToken();
        assert equal.type == TokenType.EQUAL;
        ConstValue();

        buildTree(new Token(TokenType.CONST), 2);
    }

    /**
     *  ConstValue  --> <integer>
     *              --> <char>
     *              --> Name
     *      E       --> < <integer> >
     *              --> < <char> >
     */
    private void ConstValue() {
        Token nextToken = lex.readNextToken();
        if (nextToken.type == TokenType.INTEGER || nextToken.type == TokenType.CHAR) {
            lex.getNextToken();
            buildTree(nextToken, 0);
        } else {
            Name();
        }
    }

    /**
     *  Types   --> 'type' (Type ';')+  ==> 'types'
     *          -->                     ==> 'types'
     *    E     --> < 'types' E+ >
     *          --> < 'types' >
     */
    private void Types() {
        Token type = lex.readNextToken();

        if (type.type == TokenType.TYPE) {
            lex.getNextToken();
            int numType = 0;
            Type();
            numType++;

            Token semicolon = lex.getNextToken();
            assert semicolon.type == TokenType.SEMICOLON;
            while (lex.readNextToken().type == TokenType.IDENTIFIER) {
                Type();
                semicolon = lex.getNextToken();
                assert semicolon.type == TokenType.SEMICOLON;
                numType++;
            }

            buildTree(new Token(TokenType.TYPES), numType);

        } else {
            buildTree(new Token(TokenType.TYPES), 0);
        }
    }

    /**
     *  Type    --> Name '=' LitList    ==> 'type'
     *    E     --> < 'type' <identifier> E >
     */
    private void Type() {
        Name();

        Token equal = lex.getNextToken();
        assert equal.type == TokenType.EQUAL;
        LitList();

        buildTree(new Token(TokenType.TYPE), 2);
    }

    /**
     *  LitList --> '(' Name list ',' ')'   ==> 'lit'
     *    E     --> < 'lit' <identifier>+ >
     */
    private void LitList() {
        Token openBracket = lex.getNextToken();
        assert openBracket.type == TokenType.OPEN_BRACKET;
        int numName = 0;
        Name();
        numName++;

        while (lex.readNextToken().type == TokenType.COMMA) {
            lex.getNextToken();
            Name();
            numName++;
        }

        Token closeBracket = lex.getNextToken();
        assert closeBracket.type == TokenType.CLOSE_BRACKET;
        buildTree(new Token(TokenType.LIT), numName);
    }

    /**
     *  Subprogs    --> Fcn*    ==> 'subprogs'
     *    E         --> < 'subprogs' E* >
     */
    private void Subprogs() {
        int numFcn = 0;
        while (lex.readNextToken().type == TokenType.FUNCTION) {
            Fcn();
            numFcn++;
        }
        buildTree(new Token(TokenType.SUBPROGS), numFcn);
    }

    /**
     *  Fcn --> 'function' Name '(' Params ')' ':' Name ';' Consts Types Dclns Body Name ';'    ==> 'fcn'
     *   E  --> < 'fcn' <identifier> E <identifier> E E E E <identifier> >
     */
    private void Fcn() {
        Token function = lex.getNextToken();
        assert function.type == TokenType.FUNCTION;
        Name();

        Token openBracket = lex.getNextToken();
        assert openBracket.type == TokenType.OPEN_BRACKET;
        Params();

        Token closeBracket = lex.getNextToken();
        assert closeBracket.type == TokenType.CLOSE_BRACKET;

        Token colon = lex.getNextToken();
        assert colon.type == TokenType.COLON;

        Name();

        Token semicolon = lex.getNextToken();
        assert semicolon.type == TokenType.SEMICOLON;

        Consts(); Types(); Dclns(); Body(); Name();

        semicolon = lex.getNextToken();
        assert semicolon.type == TokenType.SEMICOLON;
        buildTree(new Token(TokenType.FCN), 8);
    }

    /**
     *  Params  --> Dcln list ';'   ==> 'params'
     *    E     --> < 'params' E+ >
     */
    private void Params() {
        int numDcln = 0;
        Dcln();
        numDcln++;

        while (lex.readNextToken().type == TokenType.SEMICOLON) {
            lex.getNextToken();
            Dcln();
            numDcln++;
        }

        buildTree(new Token(TokenType.PARAMS), numDcln);
    }

    /**
     *  Dclns   --> 'var' (Dcln ';')+   ==> 'dclns'
     *          -->                     ==> 'dclns'
     *    E     --> < 'dclns' Dcln+ >
     *          --> < 'dclns' >
     */
    private void Dclns() {
        Token _var = lex.readNextToken();

        if (_var.type == TokenType.VAR) {
            lex.getNextToken();

            int numDcln = 0;
            Dcln();
            numDcln++;

            Token semicolon = lex.getNextToken();
            assert semicolon.type == TokenType.SEMICOLON;

            while (lex.readNextToken().type == TokenType.IDENTIFIER){
                Dcln();
                semicolon = lex.getNextToken();
                assert semicolon.type == TokenType.SEMICOLON;
                numDcln++;
            }

            buildTree(new Token(TokenType.DCLNS), numDcln);

        } else {
            buildTree(new Token(TokenType.DCLNS), 0);
        }
    }

    /**
     *  Dcln    --> Name list ',' ':' Name  ==> 'var'
     *   E      --> < 'var' <identifier>+ <identifier> >
     */
    private void Dcln() {
        int numName = 0;
        Name();
        numName++;

        while (lex.readNextToken().type == TokenType.COMMA) {
            lex.getNextToken();
            Name();
            numName++;
        }

        Token colon = lex.getNextToken();
        assert colon.type == TokenType.COLON;

        Name();
        numName++;

        buildTree(new Token(TokenType.VAR), numName);
    }

    /**
     *  Body    --> 'begin Statement list ';' 'end' ==> 'block'
     *   E      --> < 'block' E+ >
     */
    private void Body() {
        Token begin = lex.getNextToken();
        assert begin.type == TokenType.BEGIN;
        int numStmt = 0;
        Statement();
        numStmt++;

        while (lex.readNextToken().type == TokenType.SEMICOLON) {
            lex.getNextToken();
            Statement();
            numStmt++;
        }

        Token end = lex.getNextToken();
        assert end.type == TokenType.END;

        buildTree(new Token(TokenType.BLOCK), numStmt);
    }

    /**
     *  Statement   --> Assignment
     *              --> 'output' '(' OutExp list ',' ')'                            ==> 'output'
     *              --> 'if' Expression 'then' Statement ('else' Statement)?        ==> 'if'
     *              --> 'while' Expression 'do' Statement                           ==> 'while'
     *              --> 'repeat' Statement list ';' 'until' Expression              ==> 'repeat'
     *              --> 'for' '(' ForStat ';' ForExp ';' ForStat ')' Statement      ==> 'for'
     *              --> 'loop' Statement list ';' 'pool'                            ==> 'loop'
     *              --> 'case' Expression 'of' Caseclauses OtherwiseClause 'end'    ==> 'case'
     *              --> 'read' '(' Name list ',' ')'                                ==> 'read'
     *              --> 'exit'                                                      ==> 'exit'
     *              --> 'return' Expression                                         ==> 'return'
     *              --> Body
     *              -->                                                             ==> 'null'
     *
     *     E        --> < 'output' E+ >
     *              --> < 'if' E E E? >
     *              --> < 'while' E E >
     *              --> < 'repeat' E+ E >
     *              --> < 'for' E E E >
     *              --> < 'loop' E+ >
     *              --> < 'case' E E E >
     *              --> < 'read' <identifier>+ >
     *              --> < 'exit' >
     *              --> < 'return' E >
     *              --> < 'null' >
     */
    private void Statement() {
        Token nextToken = lex.readNextToken();

        if (nextToken.type == TokenType.IDENTIFIER) {
            Assignment();

        } else if (nextToken.type == TokenType.OUTPUT) {
            Token output = lex.getNextToken();
            Token openBracket = lex.getNextToken();
            assert openBracket.type == TokenType.OPEN_BRACKET;
            int numOutExp = 0;
            OutExp();
            numOutExp++;

            while (lex.readNextToken().type == TokenType.COMMA) {
                lex.getNextToken();
                OutExp();
                numOutExp++;
            }

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;
            buildTree(output, numOutExp);

        } else if (nextToken.type == TokenType.IF) {
            Token _if = lex.getNextToken();
            Expression();

            Token then = lex.getNextToken();
            assert then.type == TokenType.THEN;
            int numStmt = 0;
            Statement();
            numStmt++;

            if (lex.readNextToken().type == TokenType.ELSE) {
                lex.getNextToken();
                Statement();
                numStmt++;
            }

            buildTree(_if, numStmt + 1);

        } else if (nextToken.type == TokenType.WHILE) {
            Token _while = lex.getNextToken();

            Expression();

            Token _do = lex.getNextToken();
            assert _do.type == TokenType.DO;

            Statement();

            buildTree(_while, 2);

        } else if (nextToken.type == TokenType.REPEAT) {
            Token repeat = lex.getNextToken();
            int numStmt = 0;
            Statement();
            numStmt++;

            while (lex.readNextToken().type == TokenType.SEMICOLON) {
                lex.getNextToken();
                Statement();
                numStmt++;
            }

            Token until = lex.getNextToken();
            assert until.type == TokenType.UNTIL;

            Expression();

            buildTree(repeat, numStmt + 1);
        } else if (nextToken.type == TokenType.FOR) {
            Token _for = lex.getNextToken();
            Token openBracket = lex.getNextToken();
            assert openBracket.type == TokenType.OPEN_BRACKET;
            ForStat();

            Token semicolon = lex.getNextToken();
            assert semicolon.type == TokenType.SEMICOLON;
            ForExp();

            semicolon = lex.getNextToken();
            assert semicolon.type == TokenType.SEMICOLON;
            ForStat();

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;
            Statement();

            buildTree(_for, 4);

        } else if (nextToken.type == TokenType.LOOP) {
            Token loop = lex.getNextToken();
            int numStmt = 0;
            Statement();
            numStmt++;

            while (lex.readNextToken().type == TokenType.SEMICOLON) {
                lex.getNextToken();
                Statement();
                numStmt++;
            }

            Token pool = lex.getNextToken();
            assert pool.type == TokenType.POOL;
            buildTree(loop, numStmt);

        } else if (nextToken.type == TokenType.CASE) {
            Token _case = lex.getNextToken();
            Expression();

            Token of = lex.getNextToken();
            assert of.type == TokenType.OF;

            int numCaseClause = 0;
            CaseClause();
            numCaseClause++;

            Token semicolon = lex.getNextToken();
            assert semicolon.type == TokenType.SEMICOLON;

            nextToken = lex.readNextToken();
            boolean hasCaseClause = nextToken.type == TokenType.INTEGER || nextToken.type == TokenType.CHAR || nextToken.type == TokenType.IDENTIFIER;
            while (hasCaseClause) {

                CaseClause();
                numCaseClause++;

                semicolon = lex.getNextToken();
                assert semicolon.type == TokenType.SEMICOLON;

                nextToken = lex.readNextToken();
                hasCaseClause = nextToken.type == TokenType.INTEGER || nextToken.type == TokenType.CHAR || nextToken.type == TokenType.IDENTIFIER;
            }

            int hasOtherwiseClause = 0;

            if (lex.readNextToken().type == TokenType.OTHERWISE) {
                OtherwiseClause();
                hasOtherwiseClause = 1;
            }


            Token end = lex.getNextToken();
            assert end.type == TokenType.END;
            buildTree(_case, 1 + numCaseClause + hasOtherwiseClause);

        } else if (nextToken.type == TokenType.READ) {
            Token read = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket.type == TokenType.OPEN_BRACKET;
            int numName = 0;
            Name();
            numName++;

            while (lex.readNextToken().type == TokenType.COMMA) {
                lex.getNextToken();
                Name();
                numName++;
            }

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;

            buildTree(read, numName);

        } else if (nextToken.type == TokenType.EXIT) {
            Token exit = lex.getNextToken();
            buildTree(exit, 0);

        } else if (nextToken.type == TokenType.RETURN) {
            Token _return = lex.getNextToken();
            Expression();
            buildTree(_return, 1);

        } else if (nextToken.type == TokenType.BEGIN) {
            Body();
        } else {
            buildTree(new Token(TokenType.NULL), 0);
        }
    }

    /**
     *  OutExp  --> Expression          ==> 'integer'
     *          --> StringNode          ==> 'string'
     *     E    --> < 'integer' E >
     *          --> < 'string' E >
     */
    private void OutExp() {
        Token token = lex.readNextToken();

        if (token.type == TokenType.STRING) {
            StringNode();
            buildTree(new Token(TokenType.STRING_NODE), 1);

        } else {
            boolean isPrimary = (token.type == TokenType.MINUS) || (token.type == TokenType.PLUS)
                    || (token.type == TokenType.NOT) || (token.type == TokenType.EOF)
                    || (token.type == TokenType.INTEGER) || (token.type == TokenType.CHAR)
                    || (token.type == TokenType.OPEN_BRACKET) || (token.type == TokenType.SUCC)
                    || (token.type == TokenType.PRED) || (token.type == TokenType.CHR)
                    || (token.type == TokenType.ORD) || (token.type == TokenType.IDENTIFIER);

            if (isPrimary) {
                Expression();
                buildTree(new Token(TokenType.INTEGER_NODE), 1);
            } else {
                // error
            }
        }
    }

    /**
     *  StringNode  --> <string>
     *      E       --> < <string> >
     */
    private void StringNode() {
        Token string = lex.getNextToken();
        buildTree(string, 0);
        assert string.type == TokenType.STRING;
    }

    /**
     *  Caseclauses --> (Caseclause ';')+;
     */
    private void Caseclauses() {

    }

    /**
     *  Caseclause  --> CaseExpression list ',' ':' Statement   ==> 'case_clause'
     *      E       --> < case_clause E+ E >
     */
    private void CaseClause() {
        int numCaseExpression = 0;
        CaseExpression();
        numCaseExpression++;

        while (lex.readNextToken().type == TokenType.COMMA) {
            lex.getNextToken();
            CaseExpression();
            numCaseExpression++;
        }

        Token colon = lex.getNextToken();
        assert colon.type == TokenType.COLON;
        Statement();

        buildTree(new Token(TokenType.CASE_CLAUSE), numCaseExpression + 1);
    }

    /**
     *  CaseExpression  --> ConstValue
     *                  --> ConstValue '..' ConstValue  ==> '..'
     *       E          --> < '..' E E >
     */
    private void CaseExpression() {
        ConstValue();

        if (lex.readNextToken().type == TokenType.DOUBLE_DOT) {
            Token dd = lex.getNextToken();
            ConstValue();
            buildTree(dd, 2);
        }
    }

    /**
     *  OtherwiseClause --> 'otherwise' Statement   ==> 'otherwise'
     *                  -->
     *          E       --> < 'otherwise' E >
     */
    private void OtherwiseClause() {
        if (lex.readNextToken().type == TokenType.OTHERWISE) {
            Token otherwise = lex.getNextToken();
            Statement();
            buildTree(otherwise, 1);
        }
    }

    /**
     *  Assignment  --> Name ':=' Expression    ==> 'assign'
     *              --> Name ':=:' Name         ==> 'swap'
     *      E       --> < 'assign'  <identifier> E >
     *              --> < 'swap'  <identifier>  <identifier> >
     */
    private void Assignment() {
        Name();

        if (lex.readNextToken().type == TokenType.ASSIGN) {
            Token assign = lex.getNextToken();
            Expression();
            buildTree(assign, 2);
        } else if (lex.readNextToken().type == TokenType.SWAP) {
            Token swap = lex.getNextToken();
            Name();
            buildTree(swap, 2);
        } else {
            // error
        }
    }

    /**
     *  ForStat --> Assignment
     *          -->                 ==> 'null'
     *     E    --> < 'null' >
     */
    private void ForStat() {
        if (lex.readNextToken().type == TokenType.IDENTIFIER) {
            Assignment();
        } else {
            buildTree(new Token(TokenType.NULL), 0);
        }
    }

    /**
     *  ForExp  --> Expression
     *          -->                 ==> 'true'
     *    E     --> < 'true' >
     */
    private void ForExp() {
        Token token = lex.readNextToken();
        boolean isPrimary = (token.type == TokenType.MINUS) || (token.type == TokenType.PLUS)
                || (token.type == TokenType.NOT) || (token.type == TokenType.EOF)
                || (token.type == TokenType.INTEGER) || (token.type == TokenType.CHAR)
                || (token.type == TokenType.OPEN_BRACKET) || (token.type == TokenType.SUCC)
                || (token.type == TokenType.PRED) || (token.type == TokenType.CHR)
                || (token.type == TokenType.ORD) || (token.type == TokenType.IDENTIFIER);
        if (isPrimary) {
            Expression();
        } else {
            buildTree(new Token(TokenType.TRUE), 0);
        }
    }

    /**
     *  Expression  --> Term
     *              --> Term '<=' Term      ==> '<='
     *              --> Term '<' Term       ==> '<'
     *              --> Term '>=' Term      ==> '>='
     *              --> Term '>' Term       ==> '>'
     *              --> Term '=' Term       ==> '='
     *              --> Term '<>' Term      ==> '<>'
     *      E       --> < '<=' E E >
     *              --> < '<' E E >
     *              --> < '>=' E E >
     *              --> < '>' E E >
     *              --> < '=' E E >
     *              --> < '<>' E E >
     */
    private void Expression() {
        Term();

        Token nextToken = lex.readNextToken();

        if (nextToken.type == TokenType.LE) {
            Token le = lex.getNextToken();
            Term();
            buildTree(le, 2);

        } else if (nextToken.type == TokenType.LT) {
            Token lt = lex.getNextToken();
            Term();
            buildTree(lt, 2);

        } else if (nextToken.type == TokenType.GE) {
            Token ge = lex.getNextToken();
            Term();
            buildTree(ge, 2);

        } else if (nextToken.type == TokenType.GT) {
            Token gt = lex.getNextToken();
            Term();
            buildTree(gt, 2);

        } else if (nextToken.type == TokenType.EQUAL) {
            Token equal = lex.getNextToken();
            Term();
            buildTree(equal, 2);

        } else if (nextToken.type == TokenType.NE) {
            Token not_equal = lex.getNextToken();
            Term();
            buildTree(not_equal, 2);

        }
    }

    /**
     *  Term    --> Factor
     *          --> Term '+' Factor     ==> '+'
     *          --> Term '-' Factor     ==> '-'
     *          --> Term 'or' Factor    ==> 'or'
     *    E     --> < '+' E E >
     *          --> < '-' E E >
     *          --> < 'or' E E >
     */
    private void Term() {
        Factor();

        Token nextToken = lex.readNextToken();

        while (nextToken.type == TokenType.PLUS) {
            Token plus = lex.getNextToken();
            Factor();
            buildTree(plus, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken.type == TokenType.MINUS) {
            Token minus = lex.getNextToken();
            Factor();
            buildTree(minus, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken.type == TokenType.OR) {
            Token or = lex.getNextToken();
            Factor();
            buildTree(or, 2);
            nextToken = lex.readNextToken();
        }
    }

    /**
     *  Factor  --> Factor '*' Primary      ==> '*'
     *          --> Factor '/' Primary      ==> '/'
     *          --> Factor 'and' Primary      ==> 'and'
     *          --> Factor 'mod' Primary      ==> 'mod'
     *          --> Primary
     *    E     --> < '*' E E >
     *          --> < '/' E E >
     *          --> < 'and' E E >
     *          --> < 'mod' E E >
     */
    private void Factor() {
        Primary();

        Token nextToken = lex.readNextToken();

        while (nextToken.type == TokenType.MULTIPLY) {
            Token multiply = lex.getNextToken();
            Primary();
            buildTree(multiply, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken.type == TokenType.DIVIDE) {
            Token divide = lex.getNextToken();
            Primary();
            buildTree(divide, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken.type == TokenType.AND) {
            Token and = lex.getNextToken();
            Primary();
            buildTree(and, 2);
            nextToken = lex.readNextToken();
        }

        while (nextToken.type == TokenType.MOD) {
            Token mod = lex.getNextToken();
            Primary();
            buildTree(mod, 2);
            nextToken = lex.readNextToken();
        }
    }

    /**
     *  Primary --> '-' Primary                         ==> '-'
     *          --> '+' Primary
     *          --> 'not' Primary                       ==> 'not'
     *          --> 'eof'                               ==> 'eof'
     *          --> Name
     *          --> <integer>
     *          --> <char>
     *          --> Name '(' Expression list ',' ')'    ==> 'call'
     *          --> '(' Expression ')'
     *          --> 'succ' '(' Expression ')'           ==> 'succ'
     *          --> 'pred' '(' Expression ')'           ==> 'pred'
     *          --> 'chr' '(' Expression ')'            ==> 'chr'
     *          --> 'ord' '(' Expression ')'            ==> 'ord'
     *     E    --> < '-' E >
     *          --> < 'not' E >
     *          --> < 'eof' >
     *          --> < 'call' <identifier> E+ >
     *          --> < 'succ' E >
     *          --> < 'pred' E >
     *          --> < 'chr' E >
     *          --> < 'ord' E >
     */
    private void Primary() {
        Token nextToken = lex.readNextToken();

        if (nextToken.type == TokenType.MINUS) {
            Token minus = lex.getNextToken();
            Primary();
            buildTree(minus, 1);

        } else if (nextToken.type == TokenType.PLUS) {
            Token plus = lex.getNextToken();
            Primary();
            buildTree(plus, 1);

        } else if (nextToken.type == TokenType.NOT) {
            Token not = lex.getNextToken();
            Primary();
            buildTree(not, 1);

        } else if (nextToken.type == TokenType.EOF) {
            Token eof = lex.getNextToken();
            buildTree(eof, 0);

        } else if (nextToken.type == TokenType.INTEGER) {
            Token integer = lex.getNextToken();
            buildTree(integer, 0);

        } else if (nextToken.type == TokenType.CHAR) {
            Token _char = lex.getNextToken();
            buildTree(_char, 0);

        } else if (nextToken.type == TokenType.OPEN_BRACKET) {
            lex.getNextToken();
            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;

        } else if (nextToken.type == TokenType.SUCC) {
            Token succ = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket.type == TokenType.OPEN_BRACKET;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;

            buildTree(succ, 1);

        } else if (nextToken.type == TokenType.PRED) {
            Token pred = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket.type == TokenType.OPEN_BRACKET;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;

            buildTree(pred, 1);

        } else if (nextToken.type == TokenType.CHR) {
            Token chr = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket.type == TokenType.OPEN_BRACKET;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;

            buildTree(chr, 1);

        } else if (nextToken.type == TokenType.ORD) {
            Token ord = lex.getNextToken();

            Token openBracket = lex.getNextToken();
            assert openBracket.type == TokenType.OPEN_BRACKET;

            Expression();

            Token closeBracket = lex.getNextToken();
            assert closeBracket.type == TokenType.CLOSE_BRACKET;

            buildTree(ord, 1);

        } else if (nextToken.type == TokenType.IDENTIFIER) {

            Name();

            Token followingToken = lex.readNextToken();
            if (followingToken.type == TokenType.OPEN_BRACKET) {
                Token openBracket = lex.getNextToken();
                int numExpr = 0;
                Expression();
                numExpr++;

                while (lex.readNextToken().type == TokenType.COMMA) {
                    lex.getNextToken();
                    Expression();
                    numExpr++;
                }

                Token closeBracket = lex.getNextToken();
                assert closeBracket.type == TokenType.CLOSE_BRACKET;

                buildTree(new Token(TokenType.CALL), numExpr + 1);
            }

        }
    }

    /**
     *  Name    --> <identifier>
     *   E      --> < <identifier> >
     */
    private void Name() {
        Token id = lex.getNextToken();
        assert id.type == TokenType.IDENTIFIER;
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
    }

}

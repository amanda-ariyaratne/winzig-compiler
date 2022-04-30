package lex;

public class Token {

    public TokenType type;

    public String name;

    public String value;

    public Token(TokenType type) {
        this.type = type;
        setName();
    }

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
        setName();
    }

    private void setName() {
        switch (type) {
            case AND:
                name = "and"; break;
            case ASSIGN:
                name = "assign"; break;
            case BEGIN:
                name = "begin"; break;
            case BEGIN_BLOCK:
                name = "{"; break;
            case BLOCK:
                name = "block"; break;
            case CALL:
                name = "call"; break;
            case CASE:
                name = "case"; break;
            case CASE_CLAUSE:
                name = "case_clause"; break;
            case CHAR:
                name = "char"; break;
            case CHR:
                name = "chr"; break;
            case CLOSE_BRACKET:
                name = ")"; break;
            case COLON:
                name = "colon"; break;
            case COMMA:
                name = "comma"; break;
            case COMMENT:
                name = "comment"; break;
            case CONST:
                name = "const"; break;
            case CONSTS:
                name = "consts"; break;
            case DCLN:
                name = "dcln"; break;
            case DCLNS:
                name = "dclns"; break;
            case DIVIDE:
                name = "/"; break;
            case DO:
                name = "do"; break;
            case DOT:
                name = "."; break;
            case DOUBLE_DOT:
                name = ".."; break;
            case ELSE:
                name = "else"; break;
            case END:
                name = "end"; break;
            case END_BLOCK:
                name = "}"; break;
            case EOF:
                name = "eof"; break;
            case EQUAL:
                name = "="; break;
            case EXIT:
                name = "exit"; break;
            case FCN:
                name = "fcn"; break;
            case FOR:
                name = "for"; break;
            case FUNCTION:
                name = "function"; break;
            case GE:
                name = ">="; break;
            case GT:
                name = ">"; break;
            case IDENTIFIER:
                name = "identifier"; break;
            case IF:
                name = "if"; break;
            case INTEGER:
                name = "integer"; break;
            case INTEGER_NODE:
                name = "integer"; break;
            case LE:
                name = "<="; break;
            case LIT:
                name = "lit"; break;
            case LOOP:
                name = "loop"; break;
            case LT:
                name = "<"; break;
            case MINUS:
                name = "-"; break;
            case MOD:
                name = "mod"; break;
            case MULTIPLY:
                name = "*"; break;
            case NAME:
                name = "name"; break;
            case NE:
                name = "NE"; break;
            case NEWLINE:
                name = "newline"; break;
            case NOT:
                name = "not"; break;
            case NULL:
                name = "<null>"; break;
            case OF:
                name = "of"; break;
            case OPEN_BRACKET:
                name = "("; break;
            case OR:
                name = "or"; break;
            case ORD:
                name = "ord"; break;
            case OTHERWISE:
                name = "otherwise"; break;
            case OUTPUT:
                name = "output"; break;
            case PARAMS:
                name = "params"; break;
            case PLUS:
                name = "+"; break;
            case POOL:
                name = "pool"; break;
            case PRED:
                name = "pred"; break;
            case PROGRAM:
                name = "program"; break;
            case READ:
                name = "read"; break;
            case REPEAT:
                name = "repeat"; break;
            case RETURN:
                name = "return"; break;
            case SEMICOLON:
                name = ";"; break;
            case STRING:
                name = "string"; break;
            case STRING_NODE:
                name = "string"; break;
            case SUBPROGS:
                name = "subprogs"; break;
            case SUCC:
                name = "succ"; break;
            case SWAP:
                name = ":=:"; break;
            case THEN:
                name = "then"; break;
            case TRUE:
                name = "true"; break;
            case TYPE:
                name = "type"; break;
            case TYPES:
                name = "types"; break;
            case UNTIL:
                name = "until"; break;
            case VAR:
                name = "var"; break;
            case WHILE:
                name = "while"; break;
            case WHITESPACE:
                name = "whitespace"; break;
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

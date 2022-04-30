package compiler;

import parser.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer {

    private Map<String, DclnRecord> dclnTable;
    private int nextAddr;
    private String errors;

    public SemanticAnalyzer() {
        initializeSemanticAnalyzer();
    }

    private void initializeSemanticAnalyzer() {
        this.dclnTable = new HashMap<>();
        this.nextAddr = 0;
        this.errors = "";
    }

    public boolean run(Node root) {
        initializeSemanticAnalyzer();
        constrain(root);
        if (!this.errors.equals("")) {
            writeErrorsToFile();
            return true;
        }
        return false;
    }

    private void constrain(Node root) {
        evaluateProgramNode(root, "");
    }

    private String evaluateNode(Node node, String label) {
        switch (node.token.type) {
            case PROGRAM: {
                return evaluateProgramNode(node, label);
            }
            case CONSTS: {
                return evaluateConstsNode(node, label);
            }
            case CONST: {
                return evaluateConstNode(node, label);
            }
            case INTEGER: {
                return evaluateIntegerNode(node, label);
            }
            case CHAR: {
                return evaluateCharNode(node, label);
            }
            case TYPES: {
                return evaluateTypesNode(node, label);
            }
            case TYPE: {
                return evaluateTypeNode(node, label);
            }
            case DCLNS: {
                return evaluateDclnsNode(node, label);
            }
            case VAR: {
                return evaluateVarNode(node, label);
            }
            case BLOCK: {
                return evaluateBlockNode(node, label);
            }
            case OUTPUT: {
                return evaluateOutputNode(node, label);
            }
            case IF: {
                return evaluateIfNode(node, label);
            }
            case WHILE: {
                return evaluateWhileNode(node, label);
            }
            case REPEAT: {
                return evaluateRepeatNode(node, label);
            }
            case FOR: {
                return evaluateForNode(node, label);
            }
            case READ: {
                return evaluateReadNode(node, label);
            }
            case NULL: {
                return evaluateNullNode(node, label);
            }
            case INTEGER_NODE: {
                return evaluateIntegerNodeNode(node, label);
            }
            case STRING_NODE: {
                return evaluateStringNodeNode(node, label);
            }
            case STRING: {
                return evaluateStringNode(node, label);
            }
            case ASSIGN: {
                return evaluateAssignNode(node, label);
            }
            case SWAP: {
                return evaluateSwapNode(node, label);
            }
            case LE: {
                return evaluateLENode(node, label);
            }
            case LT: {
                return evaluateLTNode(node, label);
            }
            case GE: {
                return evaluateGENode(node, label);
            }
            case GT: {
                return evaluateGTNode(node, label);
            }
            case EQUAL: {
                return evaluateEqualNode(node, label);
            }
            case NE: {
                return evaluateNENode(node, label);
            }
            case PLUS: {
                return evaluatePlusNode(node, label);
            }
            case MINUS: {
                return evaluateMinusNode(node, label);
            }
            case OR: {
                return evaluateOrNode(node, label);
            }
            case MULTIPLY: {
                return evaluateMultiplyNode(node, label);
            }
            case DIVIDE: {
                return evaluateDivideNode(node, label);
            }
            case AND: {
                return evaluateAndNode(node, label);
            }
            case MOD: {
                return evaluateModNode(node, label);
            }
            case NOT: {
                return evaluateNotNode(node, label);
            }
            case IDENTIFIER: {
                return evaluateIdentifierNode(node, label);
            }
            default: {
                // System.out.println(node.token.type);
                return label;
            }
        }
    }

    private void updateDcln(String identifier, boolean isConst, String type) {
        if (!dclnTable.containsKey(identifier)) {
            dclnTable.put(identifier, new DclnRecord(identifier, this.nextAddr, isConst, type));
            this.nextAddr++;
        }
    }

    private void writeErrorsToFile() {
        try {
            FileWriter file = new FileWriter("errors.txt");
            BufferedWriter output = new BufferedWriter(file);
            output.write(this.errors);
            output.close();
        }
        catch (Exception e) {
            System.out.println("Could not write to errors.txt");
        }
    }

    /**
     *  Winzig  --> 'program' Name ':' Consts Types Dclns Subprogs Body Name '.' ==> 'program'
     *    P     --> < 'program' <identifier> E E E E E <identifier> >
     */
    private String evaluateProgramNode(Node program, String type) {
        if (!program.getNthChild(1).token.value.equals(program.getNthChild(7).token.value)) {
            this.errors = this.errors + "Program names don't match\n";
        }
        for (int i = 2; i <= 6; i++) {
            evaluateNode(program.getNthChild(i), type);
        }
        return type;
    }


    private String evaluateConstsNode(Node consts, String type) {
        for (int i = 1; i <= consts.noOfChildren; i++) {
            evaluateNode(consts.getNthChild(i), type);
        }
        return type;
    }


    private String evaluateConstNode(Node constNode, String type) {
        evaluateNode(constNode.getNthChild(2), type);
        updateDcln(constNode.firstChild.token.value, true, "integer");
        return "statement";
    }

    private String evaluateIntegerNode(Node integer, String label) {
        return "integer";
    }

    private String evaluateCharNode(Node charNode, String label) {
        return "char";
    }

    private String evaluateTypesNode(Node types, String type) {
        for (int i = 1; i <= types.noOfChildren; i++) {
            evaluateNode(types.getNthChild(i), type);
        }
        return type;
    }

    private String evaluateTypeNode(Node type, String label) {
        // TODO
        return label;
    }

    /**
     *  Dclns   --> 'var' (Dcln ';')+   ==> 'dclns'
     *          -->                     ==> 'dclns'
     *    E     --> < 'dclns' Dcln+ >
     *          --> < 'dclns' >
     */
    private String evaluateDclnsNode(Node dclns, String type) {
        for (int i = 1; i <= dclns.noOfChildren; i++) {
            evaluateNode(dclns.getNthChild(i), type);
        }
        return type;
    }

    /**
     *  Dcln    --> Name list ',' ':' Name  ==> 'var'
     *   E      --> < 'var' <identifier>+ <identifier> >
     */
    private String evaluateVarNode(Node var, String type) {
        for (int i = 1; i < var.noOfChildren; i++) {
            updateDcln(var.getNthChild(i).token.value, false, var.getNthChild(var.noOfChildren).token.value);
        }
        return "var";
    }

    /**
     *  Body    --> 'begin Statement list ';' 'end' ==> 'block'
     *   E      --> < 'block' E+ >
     */
    private String evaluateBlockNode(Node block, String type) {
        for (int i = 1; i <= block.noOfChildren; i++) {
            String type1 = evaluateNode(block.getNthChild(i), type);
            if (!type1.equals("statement")) {
                this.errors = this.errors + "Illegal type for block. Statement expected.\n";
            }
        }
        return "statement";
    }

    /**
     *  Statement   --> 'output' '(' OutExp list ',' ')'                            ==> 'output'
     *     E        --> < 'output' E+ >
     */
    private String evaluateOutputNode(Node output, String type) {
        for (int i = 1; i <= output.noOfChildren; i++) {
            String type1 = evaluateNode(output.getNthChild(i), "");
            if (!type1.equals("integer") && !type1.equals("char") && !type1.equals("string")) {
                this.errors = this.errors + "Illegal type for output.\n";
            }
        }
        return "statement";
    }

    /**
     *  Statement   --> 'if' Expression 'then' Statement ('else' Statement)?        ==> 'if'
     *     E        --> < 'if' E E E? >
     */
    private String evaluateIfNode(Node ifNode, String type) {
        String type1 = evaluateNode(ifNode.firstChild, type);
        if (!type1.equals("boolean")) {
            this.errors = this.errors + "Illegal expression for if.\n";
        }

        String type2 = evaluateNode(ifNode.getNthChild(2), type);
        if (!type2.equals("statement")) {
            this.errors = this.errors + "Statement required for if.\n";
        }
        if (ifNode.noOfChildren == 3) {
            String type3 = evaluateNode(ifNode.getNthChild(3), type);
            if (!type3.equals("statement")) {
                this.errors = this.errors + "Statement required for if.\n";
            }
        }
        return "statement";
    }

    /**
     *  Statement   --> 'while' Expression 'do' Statement                           ==> 'while'
     *     E        --> < 'while' E E >
     */
    private String evaluateWhileNode(Node whileNode, String type) {
        String type1 = evaluateNode(whileNode.firstChild, type);
        if (!type1.equals("boolean")) {
            this.errors = this.errors + "Illegal expression in while.\n";
        }

        String type2 = evaluateNode(whileNode.getNthChild(2), type);
        if (!type2.equals("statement")) {
            this.errors = this.errors + "Statement required in while.\n";
        }
        return "statement";
    }

    private String evaluateRepeatNode(Node repeat, String label) {
        // TODO
        return label;
    }

    private String evaluateForNode(Node forNode, String label) {
        // TODO
        return label;
    }

    /**
     *  Statement   --> 'read' '(' Name list ',' ')'                                ==> 'read'
     *     E        --> < 'read' <identifier>+ >
     */
    public String evaluateReadNode(Node read, String type) {
        for (int i = 1; i <= read.noOfChildren; i++) {
            if (dclnTable.get(read.getNthChild(i).token.value) == null) {
                this.errors = this.errors + "Identifier " + read.getNthChild(i).token.value + " un-initialized\n";
            } else if (dclnTable.get(read.getNthChild(1).token.value).isConst) {
                this.errors = this.errors + "Cannot assign to constant identifier " + read.getNthChild(i).token.value + "\n";
            } else if (!dclnTable.get(read.getNthChild(1).token.value).type.equals("integer")) {
                this.errors = this.errors + "Illegal type for read. Integer expected.\n";
            }
        }
        return "statement";
    }

    public String evaluateNullNode(Node nullNode, String type) {
        return "statement";
    }

    /**
     *  OutExp  --> Expression          ==> 'integer'
     *     E    --> < 'integer' E >
     */
    public String evaluateIntegerNodeNode(Node integer, String type) {
        String type1 = evaluateNode(integer.firstChild, type);
        if (!type1.equals("integer")) {
            this.errors = this.errors + "Illegal integer expression.\n";
        }
        return "integer";
    }

    public String evaluateStringNodeNode(Node string, String label) {
        // TODO
        return label;
    }

    public String evaluateStringNode(Node string, String label) {
        // TODO
        return label;
    }

    /**
     *  Assignment  --> Name ':=' Expression    ==> 'assign'
     *      E       --> < 'assign'  <identifier> E >
     */
    public String evaluateAssignNode(Node assign, String type) {
        String type1 = evaluateNode(assign.getNthChild(2), type);
        if(dclnTable.get(assign.getNthChild(1).token.value) == null) {
            this.errors = this.errors + "Identifier " + assign.getNthChild(1).token.value + " un-initialized\n";
        } else if (dclnTable.get(assign.getNthChild(1).token.value).isConst) {
            this.errors = this.errors + "Cannot assign to constant identifier " + assign.getNthChild(1).token.value + "\n";
        } else if (!dclnTable.get(assign.getNthChild(1).token.value).type
                .equals(type1)) {
            this.errors = this.errors + "Illegal types for assign.\n";
        }

        return "statement";
    }

    /**
     *  Assignment  --> Name ':=:' Name         ==> 'swap'
     *      E       --> < 'swap'  <identifier>  <identifier> >
     */
    public String evaluateSwapNode(Node swap, String type) {
        if(dclnTable.get(swap.getNthChild(1).token.value) == null) {
            this.errors = this.errors + "Identifier " + swap.getNthChild(1).token.value + " un-initialized\n";
        } else if (dclnTable.get(swap.getNthChild(1).token.value).isConst) {
            this.errors = this.errors + "Cannot assign to constant identifier " + swap.getNthChild(1).token.value + "\n";
        } else if(dclnTable.get(swap.getNthChild(2).token.value) == null) {
            this.errors = this.errors + "Identifier " + swap.getNthChild(2).token.value + " un-initialized\n";
        } else if (dclnTable.get(swap.getNthChild(2).token.value).isConst) {
            this.errors = this.errors + "Cannot assign to constant identifier " + swap.getNthChild(2).token.value + "\n";
        } else if (!dclnTable.get(swap.getNthChild(1).token.value).type
                .equals(dclnTable.get(swap.getNthChild(2).token.value))) {
            this.errors = this.errors + "Illegal types for swap.\n";
        }
        return "statement";
    }

    /**
     *  Expression  --> Term '<=' Term      ==> '<='
     *      E       --> < '<=' E E >
     */
    public String evaluateLENode(Node le, String type) {
        String type1 = evaluateNode(le.getNthChild(1), type);
        String type2 = evaluateNode(le.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for less than or equal. Integer expected.\n";
        }
        return "boolean";
    }

    /**
     *  Expression  --> Term '<' Term       ==> '<'
     *      E       --> < '<' E E >
     */
    public String evaluateLTNode(Node lt, String type) {
        String type1 = evaluateNode(lt.getNthChild(1), type);
        String type2 = evaluateNode(lt.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for less than. Integer expected.\n";
        }
        return "boolean";
    }

    /**
     *  Expression  --> Term '>=' Term      ==> '>='
     *      E       --> < '>=' E E >
     */
    public String evaluateGENode(Node ge, String type) {
        String type1 = evaluateNode(ge.getNthChild(1), type);
        String type2 = evaluateNode(ge.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for greater than or equal. Integer expected.\n";
        }
        return "boolean";
    }

    /**
     *  Expression  --> Term '>' Term       ==> '>'
     *      E       --> < '>' E E >
     */
    public String evaluateGTNode(Node gt, String type) {
        String type1 = evaluateNode(gt.getNthChild(1), type);
        String type2 = evaluateNode(gt.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for greater than. Integer expected.\n";
        }
        return "boolean";
    }

    /**
     *  Expression  --> Term '=' Term       ==> '='
     *      E       --> < '=' E E >
     */
    public String evaluateEqualNode(Node equal, String type) {
        String type1 = evaluateNode(equal.getNthChild(1), type);
        String type2 = evaluateNode(equal.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for equal. Integer expected.\n";
        }
        return "boolean";
    }

    /**
     *  Expression  --> Term '<>' Term      ==> '<>'
     *      E       --> < '<>' E E >
     */
    public String evaluateNENode(Node ne, String type) {
        String type1 = evaluateNode(ne.getNthChild(1), type);
        String type2 = evaluateNode(ne.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for not equal. Integer expected.\n";
        }
        return "boolean";
    }

    /**
     *  Term    --> Term '+' Factor     ==> '+'
     *    E     --> < '+' E E >
     */
    public String evaluatePlusNode(Node plus, String type) {
        String type1 = evaluateNode(plus.getNthChild(1), type);
        String type2 = evaluateNode(plus.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for plus. Integer expected.\n";
        }
        return "integer";
    }

    /**
     *  Term    --> Term '-' Factor     ==> '-'
     *    E     --> < '-' E E >
     */
    public String evaluateMinusNode(Node minus, String type) {
        String type1 = evaluateNode(minus.getNthChild(1), type);
        if (minus.noOfChildren == 2) {
            String type2 = evaluateNode(minus.getNthChild(1), type);
            if (!type1.equals("integer") || !type2.equals("integer")) {
                this.errors = this.errors + "Illegal type for neg. Integer expected.\n";
            }
        } else {
            if (!type1.equals("integer")) {
                this.errors = this.errors + "Illegal type for neg. Integer expected.\n";
            }
        }
        return "integer";
    }

    /**
     *  Term    --> Term 'or' Factor    ==> 'or'
     *    E     --> < 'or' E E >
     */
    public String evaluateOrNode(Node or, String type) {
        String type1 = evaluateNode(or.getNthChild(1), type);
        String type2 = evaluateNode(or.getNthChild(2), type);
        if (!type1.equals("boolean") || !type2.equals("boolean")) {
            this.errors = this.errors + "Illegal type for and. Boolean expected.\n";
        }
        return "boolean";
    }

    /**
     *  Factor  --> Factor '*' Primary      ==> '*'
     *    E     --> < '*' E E >
     */
    public String evaluateMultiplyNode(Node multiply, String type) {
        String type1 = evaluateNode(multiply.getNthChild(1), type);
        String type2 = evaluateNode(multiply.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for multiply. Integer expected.\n";
        }
        return "integer";
    }

    /**
     *  Factor  --> Factor '/' Primary      ==> '/'
     *    E     --> < '/' E E >
     */
    public String evaluateDivideNode(Node divide, String type) {
        String type1 = evaluateNode(divide.getNthChild(1), type);
        String type2 = evaluateNode(divide.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for divide. Integer expected.\n";
        }
        return "integer";
    }

    /**
     *  Factor  --> Factor 'and' Primary      ==> 'and'
     *    E     --> < 'and' E E >
     */
    public String evaluateAndNode(Node and, String type) {
        String type1 = evaluateNode(and.getNthChild(1), type);
        String type2 = evaluateNode(and.getNthChild(2), type);
        if (!type1.equals("boolean") || !type2.equals("boolean")) {
            this.errors = this.errors + "Illegal type for and. Boolean expected.\n";
        }
        return "boolean";
    }

    /**
     *  Factor  --> Factor 'mod' Primary      ==> 'mod'
     *    E     --> < 'mod' E E >
     */
    public String evaluateModNode(Node mod, String type) {
        String type1 = evaluateNode(mod.getNthChild(1), type);
        String type2 = evaluateNode(mod.getNthChild(2), type);
        if (!type1.equals("integer") || !type2.equals("integer")) {
            this.errors = this.errors + "Illegal type for mod. Integer expected.\n";
        }
        return "integer";
    }

    /**
     *  Primary --> not' Primary                       ==> 'not'
     *     E    --> < 'not' E >
     */
    public String evaluateNotNode(Node not, String type) {
        String type1 = evaluateNode(not.firstChild, type);
        if (!type1.equals("boolean")) {
            this.errors = this.errors + "Illegal type for not. Boolean expected\n";
        }
        return "boolean";
    }

    /**
     *  Name    --> <identifier>
     *   E      --> < <identifier> >
     */
    public String evaluateIdentifierNode(Node id, String type) {
        if(dclnTable.get(id.token.value) == null) {
            this.errors = this.errors + "Identifier " + id.token.value + " un-initialized\n";
        }
        return "integer";
    }

}

package compiler;

import parser.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator {

    private Map<String, DclnRecord> dclnTable;
    private List<String> labelStack, opStack, op1Stack, op2Stack;
    private int labelCount = 0;
    private int nextAddr = 0;
    private String code;

    public CodeGenerator() {
        initializeCompiler();
    }

    private void initializeCompiler() {
        this.dclnTable = new HashMap<>();
        this.labelStack = new ArrayList<>();
        this.opStack = new ArrayList<>();
        this.op1Stack = new ArrayList<>();
        this.op2Stack = new ArrayList<>();
        this.labelCount = 0;
        this.nextAddr = 0;
        this.code = "";
    }

    public void run(Node root) {
        initializeCompiler();
        generateCode(root);
        writeCodeToFile();
    }

    private void generateCode(Node root) {
        String finalLabel = evaluateProgramNode(root, "");
        generateCodeLine(String.valueOf(finalLabel), "HALT", "", "");
    }

    private void generateCodeLine(String label, String op, String op1, String op2) {
        labelStack.add(label);
        opStack.add(op);
        op1Stack.add(op1);
        op2Stack.add(op2);
        this.code = this.code + label + "\t" + op + "\t" + op1 + "\t" + op2 + "\n";
        // System.out.println(label + " " + op + " " + op1 + " " + op2);
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

    private String getNextLabel() {
        labelCount++;
        return "L" + labelCount;
    }

    private void updateDcln(String identifier, boolean isConst, String type) {
        if (!dclnTable.containsKey(identifier)) {
            dclnTable.put(identifier, new DclnRecord(identifier, this.nextAddr, isConst, type));
            this.nextAddr++;
        }
    }

    private void writeCodeToFile() {
        try {
            FileWriter file = new FileWriter("code.txt");
            BufferedWriter output = new BufferedWriter(file);
            output.write(this.code);
            output.close();
        }
        catch (Exception e) {
            System.out.println("Could not write to code.txt");
        }
    }

    /**
     *  Winzig  --> 'program' Name ':' Consts Types Dclns Subprogs Body Name '.' ==> 'program'
     *    P     --> < 'program' <identifier> E E E E E <identifier> >
     */
    private String evaluateProgramNode(Node program, String label) {
        String curLabel = label;
        for (int i = 2; i <= 6; i++) {
            curLabel = evaluateNode(program.getNthChild(i), curLabel);
        }
        return curLabel;
    }


    private String evaluateConstsNode(Node consts, String label) {
        if (consts.noOfChildren == 0) {
            return label;
        } else {
            String curLabel = label;
            for (int i = 1; i <= consts.noOfChildren; i++) {
                curLabel = evaluateNode(consts.getNthChild(i), curLabel);
            }
            return curLabel;
        }
    }

    private String evaluateConstNode(Node constNode, String label) {
        String curLabel = evaluateNode(constNode.getNthChild(2), label);
        updateDcln(constNode.firstChild.token.value, true, "integer");
        generateCodeLine("", "SGV", String.valueOf(dclnTable.get(constNode.firstChild.token.value).addr), "");
        return curLabel;
    }

    private String evaluateIntegerNode(Node integer, String label) {
        generateCodeLine(label, "LIT", integer.token.value, "");
        return label;
    }

    private String evaluateCharNode(Node charNode, String label) {
        generateCodeLine(label, "LIT", charNode.token.value, "");
        return label;
    }

    private String evaluateTypesNode(Node types, String label) {
        if (types.noOfChildren == 0) {
            return label;
        } else {
            String curLabel = label;
            for (int i = 1; i <= types.noOfChildren; i++) {
                curLabel = evaluateNode(types.getNthChild(i), curLabel);
            }
            return curLabel;
        }
    }

    private String evaluateTypeNode(Node type, String label) {
        // TODO
        return label;
    }

    private String evaluateDclnsNode(Node dclns, String label) {
        if (dclns.noOfChildren == 0) {
            return label;
        } else {
            String curLabel = label;
            for (int i = 1; i <= dclns.noOfChildren; i++) {
                curLabel = evaluateNode(dclns.getNthChild(i), curLabel);
            }
            return curLabel;
        }
    }

    /**
     *  Dcln    --> Name list ',' ':' Name  ==> 'var'
     *   E      --> < 'var' <identifier>+ <identifier> >
     */
    private String evaluateVarNode(Node var, String label) {
        for (int i = 1; i < var.noOfChildren; i++) {
            updateDcln(var.getNthChild(i).token.value, false, var.getNthChild(var.noOfChildren).token.value);
            generateCodeLine("", "LIT", "0", "");
            generateCodeLine("", "SGV", String.valueOf(dclnTable.get(var.getNthChild(i).token.value).addr), "");
        }
        return label;
    }

    /**
     *  Body    --> 'begin Statement list ';' 'end' ==> 'block'
     *   E      --> < 'block' E+ >
     */
    private String evaluateBlockNode(Node block, String label) {
        String curLabel = label;
        for (int i = 1; i <= block.noOfChildren; i++) {
            curLabel = evaluateNode(block.getNthChild(i), curLabel);
        }
        return curLabel;
    }

    /**
     *  Statement   --> 'output' '(' OutExp list ',' ')'                            ==> 'output'
     *     E        --> < 'output' E+ >
     */
    private String evaluateOutputNode(Node output, String label) {
        evaluateNode(output.getNthChild(1), label);
        generateCodeLine("", "SOS", "OUTPUT", "");


        for (int i = 2; i <= output.noOfChildren; i++) {
            evaluateNode(output.getNthChild(i), "");
            generateCodeLine("", "SOS", "OUTPUT", "");
        }
        generateCodeLine("", "SOS", "OUTPUTL", "");
        return "";
    }

    /**
     *  Statement   --> 'if' Expression 'then' Statement ('else' Statement)?        ==> 'if'
     *     E        --> < 'if' E E E? >
     */
    private String evaluateIfNode(Node ifNode, String label) {
        evaluateNode(ifNode.firstChild, label);
        String l1 = getNextLabel();
        String l2 = getNextLabel();
        String l3 = getNextLabel();
        generateCodeLine("", "COND", l1, l2);
        generateCodeLine(evaluateNode(ifNode.getNthChild(2), l1), "GOTO", l3, "");
        if (ifNode.noOfChildren == 3) {
            evaluateNode(ifNode.getNthChild(3), l2);
            // generateCodeLine(evaluateNode(ifNode.getNthChild(3), l2), "NOP", "", "");
        } else {
            generateCodeLine(l2, "NOP", "", "");
        }
        return l3;
    }

    /**
     *  Statement   --> 'while' Expression 'do' Statement                           ==> 'while'
     *     E        --> < 'while' E E >
     */
    private String evaluateWhileNode(Node whileNode, String label) {
        String l1;
        if (label == "") {
            l1 = getNextLabel();
        } else {
            l1 = label;
        }
        String l2 = getNextLabel();
        String l3 = getNextLabel();
        evaluateNode(whileNode.firstChild, l1);
        generateCodeLine("", "COND", l2, l3);
        generateCodeLine(evaluateNode(whileNode.getNthChild(2), l2), "GOTO", l1, "");
        return l3;
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
    public String evaluateReadNode(Node read, String label) {
        generateCodeLine(label, "SOS", "INPUT", "");
        generateCodeLine("", "SGV", String.valueOf(dclnTable.get(read.getNthChild(1).token.value).addr), "");
        for (int i = 2; i <= read.noOfChildren; i++) {
            generateCodeLine("", "SOS", "INPUT", "");
            generateCodeLine("", "SGV", String.valueOf(dclnTable.get(read.getNthChild(i).token.value).addr), "");
        }
        return "";
    }

    public String evaluateNullNode(Node nullNode, String label) {
        return label;
    }

    public String evaluateIntegerNodeNode(Node integer, String label) {
        evaluateNode(integer.firstChild, label);
        return label;
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
    public String evaluateAssignNode(Node assign, String label) {
        evaluateNode(assign.getNthChild(2), label);
        generateCodeLine("", "SGV", String.valueOf(dclnTable.get(assign.firstChild.token.value).addr), "");
        return "";
    }

    /**
     *  Assignment  --> Name ':=:' Name         ==> 'swap'
     *      E       --> < 'swap'  <identifier>  <identifier> >
     */
    public String evaluateSwapNode(Node swap, String label) {
        generateCodeLine("", "LGV", String.valueOf(dclnTable.get(swap.getNthChild(1).token.value)), "");
        generateCodeLine("", "LGV", String.valueOf(dclnTable.get(swap.getNthChild(2).token.value)), "");
        generateCodeLine("", "SGV", String.valueOf(dclnTable.get(swap.getNthChild(1).token.value)), "");
        generateCodeLine("", "SGV", String.valueOf(dclnTable.get(swap.getNthChild(2).token.value)), "");
        return "";
    }

    public String evaluateLENode(Node le, String label) {
        evaluateNode(le.getNthChild(1), label);
        String curLabel = evaluateNode(le.getNthChild(2), "");
        generateCodeLine("", "BOP", "BLE", "");
        return curLabel;
    }

    public String evaluateLTNode(Node lt, String label) {
        evaluateNode(lt.getNthChild(1), label);
        String curLabel = evaluateNode(lt.getNthChild(2), "");
        generateCodeLine("", "BOP", "BLT", "");
        return curLabel;
    }

    public String evaluateGENode(Node ge, String label) {
        evaluateNode(ge.getNthChild(1), label);
        String curLabel = evaluateNode(ge.getNthChild(2), "");
        generateCodeLine("", "BOP", "BGE", "");
        return curLabel;
    }

    public String evaluateGTNode(Node gt, String label) {
        evaluateNode(gt.getNthChild(1), label);
        String curLabel = evaluateNode(gt.getNthChild(2), "");
        generateCodeLine("", "BOP", "BGT", "");
        return curLabel;
    }

    public String evaluateEqualNode(Node equal, String label) {
        evaluateNode(equal.getNthChild(1), label);
        String curLabel = evaluateNode(equal.getNthChild(2), "");
        generateCodeLine("", "BOP", "BEQ", "");
        return curLabel;
    }

    public String evaluateNENode(Node ne, String label) {
        evaluateNode(ne.getNthChild(1), label);
        String curLabel = evaluateNode(ne.getNthChild(2), "");
        generateCodeLine("", "BOP", "BNE", "");
        return curLabel;
    }

    public String evaluatePlusNode(Node plus, String label) {
        evaluateNode(plus.getNthChild(1), label);
        String curLabel = evaluateNode(plus.getNthChild(2), "");
        generateCodeLine("", "BOP", "BPLUS", "");
        return curLabel;
    }

    public String evaluateMinusNode(Node minus, String label) {
        evaluateNode(minus.getNthChild(1), label);
        if (minus.noOfChildren == 2) {
            evaluateNode(minus.getNthChild(1), "");
            generateCodeLine("", "BOP", "BMINUS", "");
        } else {
            generateCodeLine("", "UOP", "UNEG", "");
        }
        return label;
    }

    public String evaluateOrNode(Node or, String label) {
        evaluateNode(or.getNthChild(1), label);
        String curLabel = evaluateNode(or.getNthChild(2), "");
        generateCodeLine("", "BOP", "BOR", "");
        return curLabel;
    }

    public String evaluateMultiplyNode(Node multiply, String label) {
        evaluateNode(multiply.getNthChild(1), label);
        String curLabel = evaluateNode(multiply.getNthChild(2), "");
        generateCodeLine("", "BOP", "BMULT", "");
        return curLabel;
    }

    public String evaluateDivideNode(Node divide, String label) {
        evaluateNode(divide.getNthChild(1), label);
        String curLabel = evaluateNode(divide.getNthChild(2), "");
        generateCodeLine("", "BOP", "BDIV", "");
        return curLabel;
    }

    public String evaluateAndNode(Node and, String label) {
        evaluateNode(and.getNthChild(1), label);
        String curLabel = evaluateNode(and.getNthChild(2), "");
        generateCodeLine("", "BOP", "BAND", "");
        return curLabel;
    }

    public String evaluateModNode(Node mod, String label) {
        evaluateNode(mod.getNthChild(1), label);
        String curLabel = evaluateNode(mod.getNthChild(2), "");
        generateCodeLine("", "BOP", "BMOD", "");
        return curLabel;
    }

    public String evaluateNotNode(Node not, String label) {
        String curLabel = evaluateNode(not.firstChild, label);
        generateCodeLine("", "UOP", "UNOT", "");
        return curLabel;
    }

    public String evaluateIdentifierNode(Node id, String label) {
        generateCodeLine(label, "LGV", String.valueOf(dclnTable.get(id.token.value).addr), "");
        return label;
    }
}

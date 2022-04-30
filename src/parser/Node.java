package parser;

import lex.Token;

public class Node {

    public Token token;

    public Node firstChild;
    public Node nextSibling;
    public int noOfChildren;

    public Node(Token token, int noOfChildren) {
        this.token = token;
        this.noOfChildren = noOfChildren;
    }

    public Node getNthChild(int n) {
        Node child = firstChild;
        for (int i = 1; i < n; i++) {
            child = child.nextSibling;
        }
        return child;
    }
}

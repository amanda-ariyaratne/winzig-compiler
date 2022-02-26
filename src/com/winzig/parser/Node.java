package com.winzig.parser;

import com.winzig.lexical.analyzer.Token;

public class Node {

    Token token;

    Node firstChild;

    Node nextSibling;

    int noOfChildren;

    public Node(Token token, int noOfChildren) {
        this.token = token;
        this.noOfChildren = noOfChildren;
    }

//    @Override
//    public String toString() {
//        return "Node{" +
//                "token=" + token +
//                ", parent=" + parent +
//                ", firstChild=" + firstChild.token +
//                ", nextSibling=" + nextSibling.token +
//                '}';
//    }
}

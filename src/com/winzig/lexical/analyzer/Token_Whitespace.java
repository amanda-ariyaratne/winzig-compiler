package com.winzig.lexical.analyzer;

public class Token_Whitespace extends Token {

    public String name = "whitespace";

    public String value;

    public Token_Whitespace(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String outValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ASCII.SPACE) { outValue += "\\w"; }
            if (value.charAt(i) == ASCII.NEWLINE) { outValue += "\\n"; }
            if (value.charAt(i) == ASCII.FORM_FEED) { outValue += "\\ff"; }
            if (value.charAt(i) == ASCII.HORIZONTAL_TAB) { outValue += "\\ht"; }
            if (value.charAt(i) == ASCII.VERTICAL_TAB) { outValue += "\\vt"; }
        }
        return "Token_Whitespace{" +
                "value='" + outValue + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }
}

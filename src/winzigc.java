import compiler.CodeGenerator;
import compiler.SemanticAnalyzer;
import lex.LexicalAnalyzer;
import lex.TokenType;
import parser.Node;
import parser.Parser;

import java.io.*;
import java.util.Collections;

public class winzigc {

    public static void main(String[] args) {

        String flag = args[0];
        String input_file_path;
        String program;
        LexicalAnalyzer lex;
        Parser parser;
        Node root;

        switch (flag){
            case  "-ast":
                input_file_path = args[1];
                program = null;
                try {
                    program = readWinzigProgram(input_file_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lex = new LexicalAnalyzer(program);
                parser = new Parser(lex);
                root = parser.parse();

                writeParseTree(root);
                System.out.println("\n");

                break;
            case "-code":
                input_file_path = args[1];
                program = null;
                try {
                    program = readWinzigProgram(input_file_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lex = new LexicalAnalyzer(program);
                parser = new Parser(lex);
                root = parser.parse();

                SemanticAnalyzer sem = new SemanticAnalyzer();
                boolean hasErrors = sem.run(root);

                if (!hasErrors) {
                    CodeGenerator compiler = new CodeGenerator();
                    compiler.run(root);
                }
                break;
            default:
                System.out.println("Provided args are incompatible.");
        }

    }

    private static String readWinzigProgram(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    private static void writeParseTree(Node root) {
        writeParseTreeRecurse(root, 0);
    }

    private static void writeParseTreeRecurse(Node root, int depth) {
        if (root.token.type == TokenType.IDENTIFIER) {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) + "<identifier>(1)");
            System.out.println(String.join("", Collections.nCopies(depth + 1, ". "))
                    + root.token.value + "(0)");
        } else if (root.token.type == TokenType.INTEGER) {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) + "<integer>(1)");
            System.out.println(String.join("", Collections.nCopies(depth + 1, ". "))
                    + root.token.value + "(0)");
        } else if (root.token.type == TokenType.CHAR) {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) + "<char>(1)");
            System.out.println(String.join("", Collections.nCopies(depth + 1, ". ")) + "'"
                    + root.token.value + "'(0)");
        } else if (root.token.type == TokenType.STRING) {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) + "<string>(1)");
            System.out.println(String.join("", Collections.nCopies(depth + 1, ". ")) + "'"
                    + root.token.value + "'(0)");
        } else {
            System.out.println(String.join("", Collections.nCopies(depth, ". ")) +
                    root.token.name + "(" + root.noOfChildren + ")");
        }
        if (root.firstChild != null) {
            writeParseTreeRecurse(root.firstChild, depth + 1);
        }
        if (root.nextSibling != null) {
            writeParseTreeRecurse(root.nextSibling, depth);
        }
    }
}

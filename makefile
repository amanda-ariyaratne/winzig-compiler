JFLAGS = -g -cp . -d .
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

# This uses the line continuation character (\) for readability
# You can list these all on a single line, separated by a space instead.
# If your version of make can't handle the leading tabs on each
# line, just remove them (these are also just added for readability).
CLASSES = \
	src/lex/ASCII.java \
	src/lex/TokenType.java \
	src/lex/Token.java \
	src/lex/LexicalAnalyzer.java \
	src/parser/Node.java \
	src/parser/Parser.java \
	src/compiler/CodeGenerator.java \
	src/compiler/DclnRecord.java \
	src/compiler/SemanticAnalyzer.java \
	src/winzigc.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
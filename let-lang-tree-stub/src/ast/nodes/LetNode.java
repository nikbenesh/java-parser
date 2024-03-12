package ast.nodes;

import lexer.Token;
import lexer.TokenType;
import environment.Environment;

public class LetNode extends SyntaxNode {
    private SyntaxNode leftNode;
    private SyntaxNode rightNode;
    private Token variable;

    public LetNode(Token variable, SyntaxNode left, SyntaxNode right) {
        this.variable = variable;
        leftNode = left;
        rightNode = right;
        this.opType = TokenType.LET;
    }

    public String evaluate(Environment env) {
        return "LetNode(" + variable.toString() + ", " + leftNode.evaluate(env) + ", "
        + rightNode.evaluate(env) + ")";
    }
}

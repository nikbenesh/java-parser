package ast.nodes;
import environment.Environment;
import lexer.Token;

public class BinOpNode extends SyntaxNode {
    private SyntaxNode leftNode;
    private SyntaxNode rightNode;
    private Token operation;

    public BinOpNode(SyntaxNode left, Token op, SyntaxNode right) {
        operation = op;
        leftNode = left;
        rightNode = right;
        this.opType = op.getType();
    }

    public String evaluate(Environment env) {
        return "BinOpNode(" + leftNode.evaluate(env) + ", " + operation.toString() + ", "
        + rightNode.evaluate(env) + ")";
    }
}

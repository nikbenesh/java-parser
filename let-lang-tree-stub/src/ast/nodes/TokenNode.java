package ast.nodes;

import environment.Environment;
import lexer.Token;
import lexer.TokenType;


public class TokenNode extends SyntaxNode {
    private Token tok;

    public TokenNode() {
        this.tok = new Token();
        this.opType = TokenType.UNKNOWN;
    }

    public TokenNode(Token tok) {
        this.tok = tok;
    }

    public String evaluate(Environment env) {
        return "TokenNode(" + tok.toString() + ")";
    }
}

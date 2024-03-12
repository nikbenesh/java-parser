package ast.nodes;

import environment.Environment;
import lexer.TokenType;

/**
 * Represents the node of a syntax tree. Each node is slightly
 * different therefore, the class is abstract each derived class
 * is responsible for implementing the evaluate method for that
 * node subtype.
 *
 */
public abstract class SyntaxNode
{
  protected TokenType opType;
  public TokenType getOpType() {
    return this.opType;
  }

  public void setOpType(TokenType newType) {
    this.opType = newType;
  }

  /**
   * Evaluate the node.
   * @param env the executional environment we should evaluate the
   * node under.
   * @return the object representing the result of the evaluation.
   */
  public abstract Object evaluate(Environment env);
}

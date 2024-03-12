package lexer;

/**
 * An enumeration of token types.
 */
public enum TokenType {

  /**
   * An identifier token.
   */
   ID,

  /**
   * Add operation token.
   */
  ADD,

  /**
   * Subtract operation token.
   */
   SUB,

  /**
   * Multiply operation token.
   */
   MULT,

   /**
    * Divide operation token.
    */
  DIV,

  // real number token
  REAL,

  // integer token
  INT,

  /**
   * A left parenthesis.
   */
  LPAREN,

  /**
   * A right parenthesis
   */
  RPAREN,

  // assignment operator
  ASGN,

  // 'let' keyword
  LET,

  // 'in' keyword
  IN,

  /**
   * An unknown token.
   */
  UNKNOWN,

  /**
   * The end of the file token.
   */
  EOF
}

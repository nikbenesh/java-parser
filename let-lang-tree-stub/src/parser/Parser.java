package parser;

import lexer.Lexer;
import lexer.TokenType;
import lexer.Token;
import ast.SyntaxTree;
import ast.nodes.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Implements a generic super class for parsing files.
 */
public class Parser
{
  private Lexer lex;            // The lexer for the parser.
  private boolean errorFound;   // True if ther was a parser error.
  private boolean doTracing;    // True if we should run parser tracing.
  private boolean readNext;     // if true, then the next token is read. 
  private Token nextTok;        // The current token being analyzed.

  /**
   * Constructs a new parser for the file {@code source} by
   * setting up lexer.
   * @param src the source code file to parse.
   * @throws FileNotFoundException if the file can not be found.
   */
  public Parser(File src) throws FileNotFoundException
  {
    lex = new Lexer(src);
    errorFound = false;
    doTracing = false;
    readNext = true;
  }

  /**
   * Construct a parser that parses the string {@code str}.
   * @param str the code to evaluate.
   */
  public Parser(String str)
  {
    lex = new Lexer(str);
    errorFound = false;
    doTracing = false;
    readNext = true;
  }

  /**
   * Turns tracing on an off.
   */
  public void toggleTracing()
  {
    doTracing = !doTracing;
  }

  /**
   * Determines if the program has any errors that would prevent
   * evaluation.
   * @return true if the program has syntax errors; otherwise, false.
   */
  public boolean hasError()
  {
    return errorFound;
  }

  /**
   * Parses the file according to the grammar.
   * @return the abstract syntax tree representing the parsed program.
   */
  public SyntaxTree parse()
  {
    return new SyntaxTree(nextNode());
  }


  /************
   * Private Methods.
   *
   * It is important to remember that all of our non-terminal processing methods
   * maintain the invariant that each method leaves the next unprocessed token
   * in {@code nextTok}. This means each method can assume the value of
   * {@code nextTok} has not yet been processed when the method begins.
   ***********/

  private void unread() {
    readNext = false;
  }

  private SyntaxNode nextNode() {
    SyntaxNode leftnode, rightnode;
    nextToken();
    while (nextTok.getType() != TokenType.EOF && nextTok.getType() != TokenType.IN && nextTok.getType() != TokenType.RPAREN) {
      if (nextTok.getType() == TokenType.LET) {
        nextToken(); // variable token
        Token variable = nextTok;
        nextToken(); // assignment operator token
        if (nextTok.getType() != TokenType.ASGN) {
          logError("Let expression missing assignment!");
          logError("Parse error, unexpected token " + nextTok.toString());
          return new TokenNode();
        }
        leftnode = nextNode(); // left expression node
        nextToken(); // 'in' keyword token
        if (nextTok.getType() != TokenType.IN) {
          logError("Let expression expected in, saw " + nextTok.toString() + ".");
          logError("Parse error, unexpected token " + nextTok.toString());
          return new TokenNode();
        }
        rightnode = nextNode(); // right expression node
        return new LetNode(variable, leftnode, rightnode);
      } else if (nextTok.getType() == TokenType.REAL || nextTok.getType() == TokenType.INT || nextTok.getType() == TokenType.ID || nextTok.getType() == TokenType.LPAREN) {
        if (nextTok.getType() == TokenType.LPAREN)
          leftnode = nextNode();
        else
          leftnode = new TokenNode(nextTok);
        while (nextTok.getType() != TokenType.EOF && nextTok.getType() != TokenType.IN) {
          nextToken();
          if (nextTok.getType() == TokenType.EOF || nextTok.getType() == TokenType.IN || nextTok.getType() == TokenType.RPAREN) {
            // if (nextTok.getType() != TokenType.RPAREN)
              unread();
            return leftnode;
          }
          Token op = nextTok;
          nextToken();
          // ((op.getType() == TokenType.ADD || op.getType() == TokenType.SUB) && 
          // (lex.nextToken().getType() == TokenType.MULT || lex.nextToken().getType() == TokenType.DIV)))
          
          /* if (op.getType() == TokenType.ADD) {
            rightnode = nextNode();
          } else if (op.getType() == TokenType.SUB) {
            rightnode = nextNode();
            if (rightnode.getOpType() == TokenType.ADD)
              rightnode.setOpType(TokenType.SUB);
            else if (rightnode.getOpType() == TokenType.SUB)
              rightnode.setOpType(TokenType.ADD);
          } else */ if (nextTok.getType() == TokenType.LPAREN) {
            rightnode = nextNode();
            nextToken();
          } else
            rightnode = new TokenNode(nextTok);
          // if (op.toString() == "EOF" || op.getType() == TokenType.IN)
          //   return leftnode;
          leftnode = new BinOpNode(leftnode, op, rightnode);
          // if ((lex.nextToken().getType() == TokenType.ADD || lex.nextToken().getType() == TokenType.SUB) && 
          // (op.getType() == TokenType.MULT || op.getType() == TokenType.DIV))
          //   return leftnode;
        }
        // if (nextTok.getType() == TokenType.RPAREN)
        //   unread();
        return leftnode;
      } else {
        logError("Unexpected token " + nextTok.toString());
        return new TokenNode();
      }
    }
    logError("Unexpected token " + nextTok.toString());
    return new TokenNode();
  }

  /**
   * Logs an error to the console.
   * @param msg the error message to dispaly.
   */
   private void logError(String msg)
   {
     System.err.println("Error (" + lex.getLineNumber() + "): " + msg);
     errorFound = true;
   }

   /**
    * This prints a message to the screen on if {@code doTracing} is
    * true.
    * @param msg the message to display to the screen.
    */
    private void trace(String msg)
    {
      if (doTracing)
        System.out.println(msg);
    }

    /**
     * Gets the next token from the lexer potentially logging that
     * token to the screen.
     */
    private void nextToken()
    {
      if (!readNext) {
        readNext = true;
        return;
      }
      nextTok = lex.nextToken();
      while (nextTok.getType() == TokenType.UNKNOWN)
        nextTok = lex.nextToken();

      if (doTracing)
        System.out.println("nextToken: " + nextTok);

    }

}

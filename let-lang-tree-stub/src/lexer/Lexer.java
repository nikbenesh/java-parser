package lexer;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
* This file implements a basic lexical analyzer.
*/
public class Lexer
{
	private BufferedReader input;      // The input to the lexer.
	private char nextChar;             // The next character read.
	private boolean skipRead;          // Whether or not to skip the next char
																		   // read.
	private long currentLineNumber;    // The current line number being processed.

	private enum CharacterClass {LETTER, DIGIT, WHITE_SPACE, OTHER, END};
	CharacterClass nextClass; // The character class of the nextChar.

	/**
	   * Constructs a new lexical analyzer whose source
	   * input is a file.
	   * @param file the file to open for lexical analysis.
	   * @throws FileNotFoundException if the file can not be opened.
	   */
	public Lexer(File file) throws FileNotFoundException
	{
		input = new BufferedReader(new FileReader(file));
		currentLineNumber = 1;
	}

	/**
	   * Constructs a new lexical analyzer whose source is a string.
	   * @param input the input to lexically analyze.
	   */
	   public Lexer(String input)
	   {
		   this.input = new BufferedReader(new StringReader(input));
		   currentLineNumber = 1;
	   }

	   /**
		* Gets the next token from the stream.
		* @return the next token.
		*/
	   public Token nextToken()
	   {
		   String value = "";   // The value to be associated with the token.

		   getNonBlank();
		   switch (nextClass)
		   {
			   // The state where we are recognizing identifiers.
			   // Regex: [A-Za-Z][0-9a-zA-z]*
			   case LETTER:
				   value += nextChar;
				   getChar();

				   // Read the rest of the identifier.
				   while (nextClass == CharacterClass.DIGIT ||
						   nextClass == CharacterClass.LETTER)
				   {
					   value += nextChar;
					   getChar();
				   }
				   unread(); // The symbol just read is part of the next token.

				   if (value.equals("let"))
					   return new Token(TokenType.LET, "");
				   if (value.equals("in"))
					   return new Token(TokenType.IN, "");

				   return new Token(TokenType.ID, value);

			   // The state where we are recognizing numbers
			   case DIGIT:
				   value  += nextChar;
				   getChar();
				   while (nextClass == CharacterClass.DIGIT || (nextChar == '.' && !value.contains("."))) {
					   value += nextChar;
					   getChar();
				   }
				   unread();
				   if (value.contains("."))
					   return new Token(TokenType.REAL, value);
				   else
					   return new Token(TokenType.INT, value);

			   // Handles all special character symbols.
			   case OTHER:
				   return lookup();

			   // We reached the end of our input.
			   case END:
				   return new Token(TokenType.EOF, "");

			   // This should never be reached.
			   default:
				   return new Token(TokenType.UNKNOWN, "");
		   }
	   }

	   /**
		* Get the current line number being processed.
		* @return the current line number being processed.
		*/
	   public long getLineNumber()
	   {
		   return currentLineNumber;
	   }

	   /************
		* Private Methods
		************/

		/**
		   * Processes the {@code nextChar} and returns the resulting token.
		   * @return the new token.
		   */
		private Token lookup()
		{
			String value = "";

			switch(nextChar)
			{
				case '+':
				   return new Token(TokenType.ADD, "");
				case '-':
				   return new Token(TokenType.SUB, "");
				case '*': // Get ready for next token.
				   return new Token(TokenType.MULT, "");
				case '/': // Get ready for next token.
				   return new Token(TokenType.DIV, "");
				case '(':
				   return new Token(TokenType.LPAREN, "");
				case ')':
				   return new Token(TokenType.RPAREN, "");
			   case ':':
				   getChar();
				   if (nextChar == '=')
					   return new Token(TokenType.ASGN, "");
				   else {
					   unread();
					   return new Token(TokenType.UNKNOWN, ":");
				   }
				default:
				   return new Token(TokenType.UNKNOWN, String.valueOf(nextChar));
			}
		}

		/**
		   * Gets the next character from the buffered reader. This updates
		   * potentially both {@code nextChar} and {@code nextClass}.
		   */
		private void getChar()
		{
			int c = -1;

			// Handle the unread operation.
			if (skipRead)
			{
				skipRead = false;
				return;
			}

			try {
				c = input.read();
			}
			catch(IOException ioe)
			{
				System.err.println("Internal error (getChar()): " + ioe);
				nextChar = '\0';
				nextClass = CharacterClass.END;
			}

			if (c == -1) // If there is no character to read, we've reached the end.
			{
			   nextChar = '\0';
			   nextClass = CharacterClass.END;
			   return;
			}

			// Set the character and determine it's class.
			nextChar = (char)c;
			if (Character.isLetter(nextChar))
			   nextClass = CharacterClass.LETTER;
			else if (Character.isDigit(nextChar))
			   nextClass = CharacterClass.DIGIT;
			else if (Character.isWhitespace(nextChar))
			   nextClass = CharacterClass.WHITE_SPACE;
			else
				   nextClass = CharacterClass.OTHER;

			// Update the line counter for error checking.
			if (nextChar == '\n')
			   currentLineNumber++;
		}

		/**
		* Gets the next non-blank character.  This updates
		* potentially both {@code nextChar} and {@code nextClass}.
		*/
		private void getNonBlank()
		{
			getChar();

			while (nextClass != CharacterClass.END &&
					   Character.isWhitespace(nextChar))
					   getChar();
		}

		/**
		* Save the previous character for a future read operation.
		*/
		private void unread() {
			skipRead = true;
		}

}

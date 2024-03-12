import java.util.Scanner;
import java.io.File;
import parser.Parser;
import ast.SyntaxTree;

import java.io.FileNotFoundException;

/**
 * This provides a simple front end to a recursie descent parser for
 * a toy language.
 */
public class Driver {
	 /**
		* The entry point.
		* @param args the array of strings that represnt the command line arguments.
		*/
	public static void main(String[] args)
	{
		 Parser parse;
		 SyntaxTree ast;

		 // Determine if we are looking at file or command line.
		//  if (args.length != 1)
		//  {
		//    System.err.println("Usage: let-lang <filename>");
		//    System.exit(1);
		//  }
		try
		{
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter file name: ");
			String filename = sc.nextLine();
			parse = new Parser(new File(filename));

			parse.toggleTracing();      // Uncomment to turn on debug tracing.

			ast = parse.parse();
			if (!parse.hasError())
			{
				System.out.println("Parse successful!");
				System.out.println("Result: " + ast.evaluate());
			}
			else
				System.out.println("Parse failed.");
		}
		catch (FileNotFoundException ex)
		{
			System.err.println(ex);
			System.exit(1);
		}
		 
	}
}

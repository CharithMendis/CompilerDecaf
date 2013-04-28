package decaf;

import java.io.*;
import antlr.*;
import ast.ASProgram;
import java6035.tools.CLI.*;
import semantic.DebugVisitor;
import semantic.SemanticVisitor;
import semantic.SymbolTablePrinter;

class Main {
    public static void main(String[] args) throws Exception{
        try {
        	CLI.parse (args, new String[0]);
        	
        	InputStream inputStream = args.length == 0 ?
                    System.in : new java.io.FileInputStream(CLI.infile);

                
                
                if (CLI.target == CLI.SCAN)
        	{
        		DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		Token token;
        		boolean done = false;
        		while (!done)
        		{
        			try
        			{
		        		for (token=lexer.nextToken(); token.getType()!=DecafParserTokenTypes.EOF; token=lexer.nextToken())
		        		{
		        			String type = "";
		        			String text = token.getText();
                                                
                                                //System.out.println(token.getType());
		
		        			switch (token.getType())
		        			{
		        			case DecafScannerTokenTypes.ID:
		        				type = " IDENTIFIER";
		        				break;
                                                case DecafScannerTokenTypes.CHAR:
                                                        type = " CHARLITERAL";
                                                        break;
                                                case DecafScannerTokenTypes.STRING:
                                                    type = " STRINGLITERAL";
                                                    break;
		        			
                                                case DecafScannerTokenTypes.INT_VAL:
                                                    type = " INTLITERAL";
                                                    break;
                                                case DecafScannerTokenTypes.TK_false:
                                                case DecafScannerTokenTypes.TK_true:
                                                    type = " BOOLEANLITERAL";
                                                    break;
		        			}
                                                
		        			
                                                
		        			System.out.println (token.getLine() + type + " " + text);
		        		}
		        		done = true;
        			} catch(Exception e) {
        	        	// print the error:
                            String name = CLI.infile;
                            File f = new File(name);
        	            System.out.println(f.getName() +" "+e);
        	            lexer.consume ();
        	        }
        		}
        	}
        	else if (CLI.target == CLI.PARSE || CLI.target == CLI.DEFAULT)
        	{
                        try{
        		DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		DecafParser parser = new DecafParser (lexer);
                        ASProgram p = parser.program();
                        p.accept(new DebugVisitor(),0);
                        SemanticVisitor v = new SemanticVisitor();
                        p.acceptWithReturn(v);
                        SymbolTablePrinter print = new SymbolTablePrinter(v.top);
                        print.print();
                        System.exit(0);
                        
                        } catch(Exception e) {
                            System.out.println(e);
                            throw e;
                            //System.exit(1);
                        }
                        
                        //AST ast = parser.getAST();
                        //System.out.println(ast.toStringList());
                       
        	}
        	
        } catch(Exception e) {
        	// print the error:
            System.out.println(CLI.infile+" "+e);
            throw e;
        }
    }
   
}


package decaf;

import java.io.*;
import antlr.*;
import ast.ASProgram;
import codegen.CodeGen;
import ir.low.IRLContainer;
import java6035.tools.CLI.*;
import semantic.ASTPrinter;
import semantic.IRLGenerator;
import semantic.IRPrinter;
import semantic.SemException;
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
        	else if (CLI.target == CLI.PARSE || CLI.target == CLI.INTER || CLI.target == CLI.DEFAULT)
        	{
                  try{
        		DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		DecafParser parser = new DecafParser (lexer);
                        ASProgram p = parser.program();
                        
                        if(CLI.target == CLI.INTER  || CLI.target == CLI.DEFAULT){
                            
                            String name = CLI.infile;
                            File f = new File(name);
                            
                            SemanticVisitor v = new SemanticVisitor(f.getName(),true);
                            p.accept(v);
                            
                            if(CLI.debug){
                                System.out.println("\n***************AST*******************");
                                p.accept(new ASTPrinter(),0);
                                System.out.println("\n***************SYMBOL TABLE*******************");
                                SymbolTablePrinter print = new SymbolTablePrinter(v.top);
                                print.print();
                                System.out.println("\n***************IR*******************");
                                p.accept(new IRPrinter(),0);
                            }

                        }
                        System.exit(0);
                        } catch(Exception e) {
                            System.out.println(e);
                            //System.exit(1);
                            throw e;
                            
                        }
        	}
                else if(CLI.target == CLI.ASSEMBLY){
                    try{
        		DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		DecafParser parser = new DecafParser (lexer);
                        ASProgram p = parser.program();
                        
                            
                            String name = CLI.infile;
                            File f = new File(name);
                            
                            SemanticVisitor v = new SemanticVisitor(f.getName(),true);
                            p.accept(v);
                            if(v.noOfErrors!=0){
                                throw new SemException();
                            }
                            
                            
                            IRLGenerator irg = new IRLGenerator(v.top);
                            p.acceptWithReturn(irg);
                            //get the container of the low level IR out
                            IRLContainer ircon = irg.currentContainer;
                            //generate code
                            CodeGen gen = new CodeGen(ircon,"out.s",CLI.debug);
                            
                            //build the IR remaining parts
                            gen.buildIRL();
                            //print it
                            gen.printIRL();
                            //generate the assembly codes
                            gen.generateCode();
                            
                            System.exit(0);
                        } catch(Exception e) {
                            System.out.println(e);
                            //System.exit(1);
                            throw e;
                            
                        }
                }
                
        	
        } catch(Exception e) {
            throw e;
        }
    }
   
}


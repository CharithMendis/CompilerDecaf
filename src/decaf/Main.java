package decaf;

import java.io.*;
import antlr.*;
import antlr.collections.AST;
import java6035.tools.CLI.*;

class Main {
    public static void main(String[] args) {
        try {
        	CLI.parse (args, new String[0]);
        	
        	InputStream inputStream = args.length == 0 ?
                    System.in : new java.io.FileInputStream(CLI.infile);
                
                if(CLI.allScanner== true){
                    testAllScanner();
                }

                else if (CLI.target == CLI.SCAN)
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
		        			
                                                case DecafScannerTokenTypes.INT:
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
        		DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		DecafParser parser = new DecafParser (lexer);
                        parser.program();
                        AST ast = parser.getAST();
                        AST s = ast.getNextSibling();
                        
                        while(s!=null){
                                System.out.println(s.toString());
                                s = s.getNextSibling();
                        }
                       
        	}
        	
        } catch(Exception e) {
        	// print the error:
            System.out.println(CLI.infile+" "+e);
        }
    }
    
    public static void testAllScanner(){
        
        String [] files = {"char1","char2","char3","char4","char5","char6","char7","char8","char9","hexlit1","hexlit2",
                            "hexlit3","id1","id2","id3","number1","number2","op1","op2","string1","string2","string3",
                            "tokens1","tokens2","tokens3","tokens4","ws1"};
        
        for(int i=0;i<files.length;i++){
        
            File f1 =  new File(System.getProperty("user.dir"));
            String s = f1.getParent();
            String check = s + "\\scanner\\output\\" +files[i] + ".out"; 
            s += "\\scanner\\" + files[i];
            
            
            try{
                 InputStream inputStream = new FileInputStream(s); 
                 
                 String [] seq = new String[200];
                 int filled = 0;
                 
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

                                         case DecafScannerTokenTypes.INT:
                                             type = " INTLITERAL";
                                             break;
                                         case DecafScannerTokenTypes.TK_false:
                                         case DecafScannerTokenTypes.TK_true:
                                             type = " BOOLEANLITERAL";
                                             break;
                                         }


                                            String line = token.getLine() + type + " " + text;
                                            seq[filled++] = line;
                                 }
                                 done = true;
                         } catch(Exception e) {
                             String name = s;
                             File f = new File(name);
                             seq[filled++] = f.getName() +" "+e;
                             lexer.consume ();
                         } 
                 }
                 
                 
                 
                 BufferedReader checkStream = new BufferedReader(new FileReader(check));
                 int read = 0;
                 int ok = 1;
                 String readline;
                 while((readline=checkStream.readLine())!=null){
                     
                     if(read > filled){
                         ok=0;
                         break;
                     }
                     if(!seq[read++].equals(readline)){
                         
                         ok=0;
                         break;
                     }
                 }
                 
                 if(ok == 1 && read == filled){
                     System.out.println(files[i]+" ok");
                 }
                 else{
                     System.out.println(files[i]+" error");
                     System.out.println(seq[read-1] + " " + readline);
                 }
                 
                 
             }catch(Exception e){
                System.err.println(e);
            }
        }
    }
}


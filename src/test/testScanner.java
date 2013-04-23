/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import antlr.Token;
import org.junit.Test;
import static org.junit.Assert.*;
import decaf.DecafScanner;
import decaf.DecafScannerTokenTypes;
import decaf.DecafParser;
import decaf.DecafParserTokenTypes;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
/**
 *
 * @author Charith
 */
public class testScanner {
    
    
    @Test 
    public void testAllParser(){
        
        //populate files
        String[] files = new String[100];
        int filled = 0;
        
        final int ILLEGAL = 20;
        final int LEGAL = 18;
        
        //fill the array
        for(int i=1;i<=ILLEGAL;i++){
            if(i<10){
                files[filled++] = "illegal-0"+Integer.toString(i);
            }
            else{
                files[filled++] = "illegal-"+Integer.toString(i);
            }
        }
        
        for(int i=1;i<=LEGAL;i++){
            if(i<10){
                files[filled++] = "legal-0"+Integer.toString(i);
            }
            else{
                files[filled++] = "legal-"+Integer.toString(i);
            }
        }
        
        for(int i=0;i<filled;i++){

            File f1 =  new File("D:\\MIT\\Compiler\\6-035-spring-2010\\6-035-spring-2010\\project\\p1files\\provided\\skeleton");
            String s = f1.getParent();
            s += "\\parser\\" + files[i];
            
            String type = files[i].split("-")[0];
            //System.out.println(type);

            InputStream inputStream = null;
            try{
              inputStream = new FileInputStream(s); 
            }
             catch(Exception e){
                 System.out.println(e);
            }
            
            try{
             DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
             DecafParser parser = new DecafParser (lexer);
             parser.program(); 
             System.out.println("legal"+ " " + files[i]);
             assertEquals(type,"legal");
             
             
            }
            catch(Exception e){
                
                System.out.println(e);
                System.out.println("illegal" + " " + files[i]);
                assertEquals(type,"illegal");
            }
        }
    }

    @Test
    public void testAllScanner(){
        
        String [] files = {"char1","char2","char3","char4","char5","char6","char7","char8","char9","hexlit1","hexlit2",
                            "hexlit3","id1","id2","id3","number1","number2","op1","op2","string1","string2","string3",
                            "tokens1","tokens2","tokens3","tokens4","ws1"};
        
        for(int i=0;i<files.length;i++){
        
            File f1 =  new File("D:\\MIT\\Compiler\\6-035-spring-2010\\6-035-spring-2010\\project\\p1files\\provided\\skeleton");
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

                                         case DecafScannerTokenTypes.INT_VAL:
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
                 
                 String readline;
                 while((readline=checkStream.readLine())!=null){
                     
                     assertFalse(read > filled);
                     assertEquals(seq[read],readline);
                     read++;
                 }

             }catch(Exception e){
                System.err.println(e);
            }
        }
    }
}

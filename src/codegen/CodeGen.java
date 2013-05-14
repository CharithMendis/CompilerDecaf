/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ir.low.IRLContainer;
import ir.low.STRING;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Charith
 */
public class CodeGen {
    
    IRLContainer container;
    BufferedWriter buf;
    boolean debug;
    
    //globals for code generation
    ArrayList<STRING> strings;

    public CodeGen(IRLContainer container,String name,boolean debug) {
        this.container = container;
        
        CGExceptions except = new CGExceptions();
        
        this.container.addActivation(except.getOutOfBoundsException(this.container.top));
        this.container.addActivation(except.getNoReturnException(this.container.top));
        
        this.debug = debug;
        try {
            buf = new BufferedWriter(new FileWriter(name));
        } catch (Exception ex) {
            Logger.getLogger(CodeGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //print
    public void printIRL(){
        try {
            //container.accept(new IRLPrinter(), null);
        } catch (Exception ex) {
            Logger.getLogger(CodeGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //IRL passes
    public void buildIRL(){
        try {
            //1. string labeling
            IRStringLabel_1 irs = new IRStringLabel_1();
            container.accept(irs, null);
            strings = irs.getStringList();
            //2. parameter and local labeling
            IRVariableAllocator_2 irv = new  IRVariableAllocator_2(container,debug);
            irv.traverse();
            //3. labels
            container.accept(new IRLabelAllocator_3(), null);
            //4. temps
            container.accept(new IRTempAllocator3_4(debug), null);
            
        } catch (Exception ex) {
            Logger.getLogger(CodeGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //actual code printing
    public void generateCode(){
        try {
            
            
            //1. generate the global variable code
            container.accept(new CGGlobalDecl_1(buf),null);
            buf.newLine();
            //2. generate strings
            (new CGString_2(buf,strings)).print();
            //3. generate final code
            container.accept(new CGActivation3_3(buf), null);
           
            

        } catch (Exception ex) {
            Logger.getLogger(CodeGen.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                buf.flush();
                buf.close();
            } catch (IOException ex) {
                Logger.getLogger(CodeGen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }        
    
    
}

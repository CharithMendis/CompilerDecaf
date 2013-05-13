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
    
    //globals for code generation
    ArrayList<STRING> strings;

    public CodeGen(IRLContainer container,String name) {
        this.container = container;
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
            //string labeling
            IRStringLabel_1 irs = new IRStringLabel_1();
            container.accept(irs, null);
            strings = irs.getStringList();
            //parameter and local labeling
            IRVariableAllocator_2 irv = new  IRVariableAllocator_2(container,true);
            irv.traverse();
            //labels
            container.accept(new IRLabelAllocator_3(), null);
            //temps
            //container.accept(new IRTempAllocator_4(true), null);
            //container.accept(new IRTempAllocator2_4(false), null);
            //container.accept(new IRTempAllocator2_5(false), null);
            container.accept(new IRTempAllocator3_4(false), null);
            
        } catch (Exception ex) {
            Logger.getLogger(CodeGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //actual code printing
    public void generateCode(){
        try {
            
            
            //generate the global variable code
            container.accept(new CGGlobalDecl_1(buf),null);
            buf.newLine();
            //generate strings
            (new CGString_2(buf,strings)).print();
            //generate the strings of final code
            //container.accept(new CGActivation_3(buf), null);
            //container.accept(new CGActivation2_3(buf), null);
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

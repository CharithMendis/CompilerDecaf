/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ir.low.IRLContainer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Charith
 */
public class CodeGen {
    
    IRLContainer container;
    BufferedWriter buf;

    public CodeGen(IRLContainer container,String name) {
        this.container = container;
        buf = null;
        try {
            buf = new BufferedWriter(new FileWriter(name));
        } catch (Exception ex) {
            Logger.getLogger(CodeGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //IRL passes
    public void buildIRL(){
        
    }
    
    //actual code printing
    public void generateCode(){
        try {
            
            
            //generate the global variable code
            container.accept(new CGGlobalDecl_1(buf));
            

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

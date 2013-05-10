/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ir.low.CONST;
import ir.low.IRLEx;
import ir.low.IRLTemp;

/**
 *
 * @author Charith
 */
public class CGTranslate {
    
   
    
    
    String getArithmeticCommands(String op){
        switch(op){
            case "+":
                return "addl";
            case "-":
                return "subl";
            case "*":
                return "imull";
            default:
                return "idivl";
        }
    }
    
    
    public String movCode(String src,String dest){
        return "\tmovl " + src + ", " + dest + "\n";
    }
    
    public String addSubMulCode(String op,String src,String dest){
        return "\t" + getArithmeticCommands(op) + " " + src + ", " + dest + "\n";
    }
    
    public String divModCode(String op,String src){
        return "\t" + getArithmeticCommands(op) + " " + src + "\n";
    }
    
    public String pushCode(String dest){
        return "\tpush " + dest + "\n";
    }
    
    public String callCode(String label){
        return "\tcall " + label + "\n";
    }
    
    public String returnCode(){
        return "\tleave\n\tret\n";
    }
    
}

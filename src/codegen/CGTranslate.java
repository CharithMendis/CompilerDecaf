/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;
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
    
    String getJumpCommands(String op){
        switch(op){
            case "==":
                return "je";
            case "!=":
                return "jne";
            case "<":
                return "jl";
            case "<=":
                return "jle";
            case ">":
                return "jg";
            case ">=":
                return "jge";
            case "!":
                return "jz";
            default:
                return "jmp";
        }
    }
    
    String getOppositeJumpCommands(String op){
            switch(op){
                case "==":
                    return "jne";
                case "!=":
                    return "je";
                case "<":
                    return "jge";
                case "<=":
                    return "jg";
                case ">":
                    return "jle";
                case ">=":
                    return "jl";
                case "!":
                    return "jnz";
                default:
                    return "jmp";
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
    
    public String jumpCode(String jump,String where,boolean opposite){
        if(!opposite){
            return "\t" + getJumpCommands(jump) + " " + where + "\n";
        }
        else{
            return "\t" + getOppositeJumpCommands(jump) + " " + where + "\n";
        }
    }
    
}

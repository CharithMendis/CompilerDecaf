/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import codegen.VisitorIR;

/**
 *
 * @author Charith
 */
public class IRLTemp extends IRLEx{
    
    
    public static final int REGCNT = 8;

    public int loc;
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
    public String getRegister(){
        switch(loc){
            case 1:
                return "%eax";
            case 2:
                return "%ebx";
            case 3:
                return "%ecx";
            case 4:
                return "%edx";
            case 5:
                return "%esp";
            case 6:
                return "%ebp";
            case 7:
                return "%esi";
            case 8:
                return "%edi";
            default:
                int actual;
                if(loc <= 0){
                    actual = -loc;
                }
                else{
                    actual = -(loc - REGCNT);
                }
                return String.valueOf(actual*4) + "(%ebp)";
        }
    }
    
    
    
}

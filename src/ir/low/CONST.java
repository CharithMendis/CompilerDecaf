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
public class CONST extends IRLEx{
    
    public int val = 0;
    
    
    public CONST(boolean b){
        if(b){
            val = 1;
        }
        else{
            val = 0;
        }
    }
    
    public CONST(int i){
        val = i;
    }
    
    public CONST(char c){
        val = c;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

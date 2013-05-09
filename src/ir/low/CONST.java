/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

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
    
}

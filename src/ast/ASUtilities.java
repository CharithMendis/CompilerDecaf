/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semantic.Visitor;

/**
 *
 * @author Charith
 */
public class ASUtilities {
    
    public static void visit(AS val,int t,Visitor v){
        Class c = val.getClass();
        c.cast(val);
        val.accept(v, t+1);
    }
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semantic.VisitorWithPara;

/**
 *
 * @author Charith
 */
public class ASUtilities {
    
    public static void visit(AS val,int t,VisitorWithPara v){
        Class c = val.getClass();
        c.cast(val);
        val.accept(v, t+1);
    }
    
    
}

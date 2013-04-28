/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semantic.Visitor;
import semantic.VisitorWithReturn;

/**
 *
 * @author Charith
 */
public class ASMethodCallS extends ASStatement{
    
    public ASMethodCall method;

    public ASMethodCallS(ASMethodCall method) {
        this.method = method;
    }
    
    
    public ASMethodCallS(ASMethodCall method, int line, int col){
         this(method);
         this.line = line;
         this.column = col;
     }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
}

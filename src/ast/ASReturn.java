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
public class ASReturn extends ASStatement{
    
    public ASExpr returnExpr;

    public ASReturn() {
        this.returnExpr = null;
    }
    
    public ASReturn(ASExpr expr){
        this.returnExpr = expr;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        if(returnExpr != null)
           ASUtilities.visit(returnExpr, t, v);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
}

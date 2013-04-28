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
    
    public ASReturn(int line,int col){
        this();
        this.line = line;
        this.column = col;
    }
    
    public ASReturn(ASExpr expr,int line,int col){
        this(expr);
        this.line = line;
        this.column = col;
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

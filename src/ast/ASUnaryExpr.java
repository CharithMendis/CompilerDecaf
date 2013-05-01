/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semantic.Visitor;
import semantic.VisitorWithPara;
import semantic.VisitorWithReturn;

/**
 *
 * @author Charith
 */
public class ASUnaryExpr extends ASExpr{
    
    public int operator;
    public String stringop;
    public ASExpr expr;
    
    public static final int MINUS = 1;
    public static final int NOT   = 2;

    public ASUnaryExpr(String op, ASExpr expr) {
        
        this.expr = expr;
        this.stringop=op;
        
        switch (op){
            case "-":
                this.operator = MINUS;
                break;
            default:
                this.operator = NOT;
                break;
        }
        
    }
    
    public ASUnaryExpr(String op, ASExpr expr,int line,int col){
        this(op,expr);
        this.line = line;
        this.column = col;
    }
    
    public void accept(VisitorWithPara v, int t) {
        v.visit(this,t);
        
    }
    
    public void accept(Visitor v) {
        v.visit(this);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
    
}

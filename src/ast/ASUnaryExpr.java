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
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        ASUtilities.visit(expr, t, v);
    }
    
    
}

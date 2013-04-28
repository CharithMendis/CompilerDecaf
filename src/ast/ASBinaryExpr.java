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
public class ASBinaryExpr extends ASExpr{
    
    public int operator;
    public String stringop;
    public ASExpr lhs,rhs;
    
    public static final int PLUS    = 1;
    public static final int MINUS   = 2;
    public static final int MULT    = 3;
    public static final int DIV     = 4;
    public static final int MOD     = 5;
    public static final int GE      = 6;
    public static final int GT      = 7;
    public static final int LT      = 8;
    public static final int LE      = 9;
    public static final int EQ      = 10;
    public static final int NEQ     = 11;
    public static final int AND     = 12;
    public static final int OR      = 13;

    public ASBinaryExpr(String op, ASExpr lhs, ASExpr rhs) {
        
        this.lhs = lhs;
        this.rhs = rhs;
        this.stringop = op;
        switch (op) {
            case "+":
                this.operator = PLUS;
                break;
            case "-":
                this.operator = MINUS;
                break;
            case "*":
                this.operator = MULT;
                break;
            case "/":
                this.operator = DIV;
                break;
            case "%":
                this.operator = MOD;
                break;
            case ">":
                this.operator = GT;
                break;
            case ">=":
                this.operator = GE;
                break;
            case "<":
                this.operator = LT;
                break;
            case "<=":
                this.operator = LE;
                break;
            case "==":
                this.operator = EQ;
                break;
            case "!=":
                this.operator = NEQ;
                break;
            case "&&":
                this.operator = AND;
                break;
            default:
                this.operator = OR;
                break;
        }
    }
    
    public ASBinaryExpr(String op, ASExpr lhs, ASExpr rhs, int line, int col){
        this(op,lhs,rhs);
        this.line = line;
        this.column = col;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        
        //visit other classes
        ASUtilities.visit(lhs, t, v);
        ASUtilities.visit(rhs, t, v);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
    
    
    
    
    
    
}

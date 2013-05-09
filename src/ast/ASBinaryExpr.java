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
    public static final int ERROR   = 14;

    public ASBinaryExpr(String op, ASExpr lhs, ASExpr rhs) {
        
        this.lhs = lhs;
        this.rhs = rhs;
        this.stringop = op;
        this.operator = getOperator(op);
    }
    
    public static int getOperator(String op){
        int retop;
        switch (op) {
            case "+":
                retop = PLUS;
                break;
            case "-":
                retop = MINUS;
                break;
            case "*":
                retop = MULT;
                break;
            case "/":
                retop = DIV;
                break;
            case "%":
                retop = MOD;
                break;
            case ">":
                retop = GT;
                break;
            case ">=":
                retop = GE;
                break;
            case "<":
                retop = LT;
                break;
            case "<=":
                retop = LE;
                break;
            case "==":
                retop = EQ;
                break;
            case "!=":
                retop = NEQ;
                break;
            case "&&":
                retop = AND;
                break;
            case "||":
                retop = OR;
                break;
            default:
                retop = ERROR;
                break;
        }
        return retop;
    }
    
    public static boolean isArithmetic(int op){
        return op==PLUS || op==MINUS || op==MULT || op==DIV || op==MOD;
    }
    
    public static boolean isConditional(int op){
        return op==GE || op==GT || op==LE || op==LT || op==EQ || op==NEQ;
    }
    
    public static boolean isRelational(int op){
        return op==AND || op==OR;
    }
    
    public ASBinaryExpr(String op, ASExpr lhs, ASExpr rhs, int line, int col){
        this(op,lhs,rhs);
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

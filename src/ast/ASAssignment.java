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
public class ASAssignment extends ASStatement{
    
    //ASLocation, assignment, ASExpr
    
    public ASLocation location;
    public String stringop;
    public int operator;
    public ASExpr expr;
    
    
    public static final int ASSIGN = 1;
    public static final int INC_ASSIGN = 2;
    public static final int DEC_ASSIGN = 3;

    public ASAssignment(String operator,ASLocation location, ASExpr expr) {
        this.location = location;
        this.expr = expr;
        this.stringop = operator;
        if(operator.equals("=")){
            this.operator = ASSIGN;
        }
        else if(operator.equals("+=")){
            this.operator = INC_ASSIGN;
        }
        else{
            this.operator = DEC_ASSIGN;
        }
    }
    
    public ASAssignment(String operator,ASLocation location, ASExpr expr, int line, int col) {
        this(operator,location, expr);
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

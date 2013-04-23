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
    
    public void accept(Visitor v, int t) {
        
        v.visit(this,t);
        
        //visit other classes
        ASUtilities.visit(location, t, v);
        ASUtilities.visit(expr, t, v);
    }
    
}

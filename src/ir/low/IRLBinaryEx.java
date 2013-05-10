/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import ast.ASBinaryExpr;

/**
 *
 * @author Charith
 */
public abstract class IRLBinaryEx extends IRLEx{
    
    
    public String stringop;
    public int operator;
    public IRLEx lhs;
    public IRLEx rhs;

    public IRLBinaryEx(String stringop, IRLEx lhs, IRLEx rhs) {
        this.stringop = stringop;
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = ASBinaryExpr.getOperator(stringop);

    }
    
    
    
}

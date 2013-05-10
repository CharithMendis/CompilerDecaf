/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import ast.ASBinaryExpr;
import codegen.VisitorIR;

/**
 *
 * @author Charith
 */
public class IRLConEx extends IRLBinaryEx{
    
    public int associated;    
    

    public IRLConEx(String stringop, IRLEx lhs, IRLEx rhs) {
        super(stringop, lhs, rhs);
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
}

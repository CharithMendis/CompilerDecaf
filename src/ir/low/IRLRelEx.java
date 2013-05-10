/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import codegen.VisitorIR;

/**
 *
 * @author Charith
 */
public class IRLRelEx extends IRLBinaryEx{
    
    
    public boolean isStored;
    
    
    
    public IRLRelEx(String stringop, IRLEx lhs, IRLEx rhs) {
        super(stringop, lhs, rhs);
        isStored = false;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

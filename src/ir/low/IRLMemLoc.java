/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import codegen.VisitorIR;
import semantic.symbol.FieldDescriptor;

/**
 *
 * @author Charith
 */
public class IRLMemLoc extends IRLEx{
    
    public IRLEx expr;  //this is null for normal variables only for array variables
    public FieldDescriptor fdes;
    
    

    public IRLMemLoc(FieldDescriptor fdes) {
        this.fdes = fdes;
        this.expr = null;
    }

    public IRLMemLoc(IRLEx expr, FieldDescriptor fdes) {
        this.expr = expr;
        this.fdes = fdes;
    }
    
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
    
}

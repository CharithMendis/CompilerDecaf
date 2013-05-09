/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import semantic.symbol.FieldDescriptor;

/**
 *
 * @author Charith
 */
public class IRLMemLoc extends IRLEx{
    
    public IRLEx expr;  //this is null for normal variables
    public FieldDescriptor fdes;
    
    

    public IRLMemLoc(FieldDescriptor fdes) {
        this.fdes = fdes;
        this.expr = null;
    }

    public IRLMemLoc(IRLEx expr, FieldDescriptor fdes) {
        this.expr = expr;
        this.fdes = fdes;
    }
    
    
    
    
    
}

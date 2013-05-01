/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.high;

import semantic.symbol.FieldDescriptor;

/**
 *
 * @author Charith
 */
public class IRStore extends IR{
    
    public FieldDescriptor fdes;

    public IRStore(FieldDescriptor fdes) {
        this.fdes = fdes;
    }
    
    
}

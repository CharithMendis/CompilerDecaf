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
public class IRLoad extends IR{
    
    FieldDescriptor fdes;

    public IRLoad(FieldDescriptor fdes) {
        this.fdes = fdes;
    }
    
    
}

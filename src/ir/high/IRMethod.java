/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.high;

import semantic.symbol.MethodDescriptor;

/**
 *
 * @author Charith
 */
public class IRMethod extends IR{
    
    MethodDescriptor mdes;

    public IRMethod(MethodDescriptor mdes) {
        this.mdes = mdes;
    }

}

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
public abstract class IRL {
    
    
    public abstract Object accept(VisitorIR v,Object o) throws Exception; 
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ir.low.IRLContainer;

/**
 *
 * @author Charith
 */
public interface VisitorIR {
    
    public void visit(IRLContainer ir) throws Exception;
    
}

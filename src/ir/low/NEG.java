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
public class NEG extends IRLEx{
    
    public IRLEx ex;
    public IRLTemp loc;

    public NEG(IRLEx ex) {
        this.ex = ex;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

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
public class IRLReturn extends IRLStm{
    
    public IRLEx ex;

    public IRLReturn(IRLEx ex) {
        this.ex = ex;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

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
public class MOV extends IRLStm{
    
    public IRLEx to;
    public IRLEx from;

    public MOV(IRLEx from, IRLEx to) {
        this.to = to;
        this.from = from;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }

    
    
}

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
public class JUMP extends IRLStm{
    
    public IRLLabel where;

    public JUMP(IRLLabel where) {
        this.where = where;
    }

    public JUMP() {
    }
    
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

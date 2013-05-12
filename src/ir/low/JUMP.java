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
    
    //for for loops -> bad practise need to change
    public boolean havePara;

    public JUMP(IRLLabel where) {
        this.where = where;
        havePara = false;
    }

    public JUMP() {
        havePara = false;
    }
    
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

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
public class CJUMP extends IRLStm{
    
    public IRLEx ex;
    public IRLLabel f;
    public IRLLabel t;
    public IRLLabel jumpAfterFalse;

    public IRLStm nextF;
    public IRLStm nextT;
    
    public int noTemp;

    public CJUMP(IRLEx ex,IRLLabel t, IRLLabel f) {
        this.ex = ex;
        this.f = f;
        this.t = t;
        this.jumpAfterFalse = new IRLLabel();
        nextF = new IRLTerStm();
        nextT = new IRLTerStm();
        
        own = null;
        noTemp = 0;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

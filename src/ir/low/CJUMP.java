/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

/**
 *
 * @author Charith
 */
public class CJUMP extends IRLStm{
    
    public IRLEx ex;
    public IRLLabel t;
    public IRLLabel f;

    public IRLStm nextF;
    public IRLStm nextT;

    public CJUMP(IRLEx ex, IRLLabel t, IRLLabel f) {
        this.ex = ex;
        this.t = t;
        this.f = f;
        nextF = new IRLTerStm();
        nextT = new IRLTerStm();
        own = null;
    }
    
    
    
}

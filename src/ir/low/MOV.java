/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

/**
 *
 * @author Charith
 */
public class MOV extends IRLStm{
    
    public IRLEx to;
    public IRLEx from;

    public MOV(IRLEx to, IRLEx from) {
        this.to = to;
        this.from = from;
    }

    
    
}

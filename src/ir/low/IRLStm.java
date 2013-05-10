/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

/**
 *
 * @author Charith
 */
public abstract class IRLStm extends IRL{
    
    public IRLStm next = null;
    public IRLLabel own = null;
    
    //this is to contain the statements that are going to be in the code
    public String code = "";
    
}

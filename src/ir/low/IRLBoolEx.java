/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

/**
 *
 * @author Charith
 */
public abstract class IRLBoolEx extends IRLBinaryEx{

    public boolean isStored;
    public IRLLabel trueLabel;
    public IRLLabel falseLabel;
    public IRLLabel jumpAfterFalse;
    
    public IRLBoolEx(String stringop, IRLEx lhs, IRLEx rhs) {
        super(stringop, lhs, rhs);
    }
    
    public abstract void store();

    
}

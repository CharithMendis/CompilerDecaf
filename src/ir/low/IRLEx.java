/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

/**
 *
 * @author Charith
 */
public abstract class IRLEx extends IRL{
    
    //only for AND/OR relational expressions
    public IRLEx trueEx;
    public IRLEx falseEx;
    
    
    //temporary for storing values
    public IRLTemp location;
    
}

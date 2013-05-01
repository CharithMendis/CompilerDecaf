/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semantic.Visitor;
import semantic.VisitorWithReturn;

/**
 *
 * @author Charith
 */
public abstract class AS {
    
    public int line;
    public int column;
    
    public ASType typeIs;
    
    abstract public void accept(Visitor v, int t);
    abstract public Object acceptWithReturn(VisitorWithReturn v);
    
    
    

        
}

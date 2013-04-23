/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semantic.Visitor;

/**
 *
 * @author Charith
 */
public class ASIntLiteral extends ASLiteral{
    
    public int integer;

    public ASIntLiteral(int integer) {
        this.integer = integer;
        
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
}

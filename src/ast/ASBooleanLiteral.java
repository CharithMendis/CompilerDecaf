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
public class ASBooleanLiteral extends ASLiteral{
    
    public boolean bool;

    public ASBooleanLiteral(boolean bool) {
        this.bool = bool;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
}

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
public class ASContinue extends ASStatement{
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
}

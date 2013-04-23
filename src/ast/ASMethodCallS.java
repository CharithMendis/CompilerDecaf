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
public class ASMethodCallS extends ASStatement{
    
    public ASMethodCall method;

    public ASMethodCallS(ASMethodCall method) {
        this.method = method;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        ASUtilities.visit(method, t, v);
    }
    
}

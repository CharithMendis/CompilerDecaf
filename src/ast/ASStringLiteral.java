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
public class ASStringLiteral extends ASCalloutArg{
    
    public String argument;

    public ASStringLiteral(String argument) {
        this.argument = argument;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
    
    
}

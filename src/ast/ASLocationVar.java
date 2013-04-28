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
public class ASLocationVar extends ASLocation{


    public ASLocationVar(String name) {
        this.name = name;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
 
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
}

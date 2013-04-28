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
public class ASContinue extends ASStatement{

    public ASContinue() {
    }

    public ASContinue(int line, int col) {
        this();
        this.column = col;
        this.line = line;
    }
    
    
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
}

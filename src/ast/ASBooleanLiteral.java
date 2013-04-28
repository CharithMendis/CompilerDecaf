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
public class ASBooleanLiteral extends ASLiteral{
    
    public boolean bool;

    public ASBooleanLiteral(boolean bool) {
        this.bool = bool;
    }
    
    public ASBooleanLiteral(boolean bool,int line,int col){
        this(bool);
        this.line = line;
        this.column = col;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
}

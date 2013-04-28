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
public class ASIntLiteral extends ASLiteral{
    
    public int integer;

    public ASIntLiteral(int integer) {
        this.integer = integer;
        
    }
    
    public ASIntLiteral(int integer, int line, int col){
       this(integer);
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

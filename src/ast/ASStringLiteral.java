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
public class ASStringLiteral extends ASCalloutArg{
    
    public String argument;

    public ASStringLiteral(String argument) {
        this.argument = argument;
    }
    
    public ASStringLiteral(String argument,int line, int col){
        this(argument);
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

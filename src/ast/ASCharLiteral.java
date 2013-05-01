/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semantic.Visitor;
import semantic.VisitorWithPara;
import semantic.VisitorWithReturn;

/**
 *
 * @author Charith
 */
public class ASCharLiteral extends ASLiteral{
    
    public char character;

    public ASCharLiteral(char character) {
        this.character = character;
    }
    
    public ASCharLiteral(char character,int line, int col){
        this(character);
        this.line = line;
        this.column = col;
    }

    public void accept(VisitorWithPara v, int t) {
        v.visit(this,t);
    }
    
    public void accept(Visitor v) {
        v.visit(this);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
}

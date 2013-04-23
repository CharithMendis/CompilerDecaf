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
public class ASCharLiteral extends ASLiteral{
    
    public char character;

    public ASCharLiteral(char character) {
        this.character = character;
    }

    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
}

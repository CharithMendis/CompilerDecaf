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
public class ASArray extends ASFieldDecl{
    
    public ASType type;
    public String name;
    public int size;

    public ASArray(ASType type, String name, int size) {
        this.type = type;
        this.name = name;
        this.size = size;
    }

    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
    
}

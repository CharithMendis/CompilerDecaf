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
public class ASVariable extends ASFieldDecl{
    
    public ASType type;
    public String name;

    public ASVariable(ASType type, String name) {
        this.type = type;
        this.name = name;
    }
    
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
    }
    
}

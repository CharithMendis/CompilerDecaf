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
public class ASVariable extends ASFieldDecl{
    
    public ASType type;
    public String name;

    public ASVariable(ASType type, String name) {
        this.type = type;
        this.name = name;
    }
    
    public ASVariable(ASType type, String name,int line,int col){
        this(type,name);
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

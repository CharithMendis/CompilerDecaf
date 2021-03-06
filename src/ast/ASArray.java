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
public class ASArray extends ASFieldDecl{
    
    public ASType type;
    public String name;
    public int size;

    public ASArray(ASType type, String name, int size) {
        this.type = type;
        this.name = name;
        this.size = size;
    }
    
    public ASArray(ASType type, String name, int size,int line,int col) {
        this(type,name,size);
        this.column = col;
        this.line = line;
    }

    //visitors
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

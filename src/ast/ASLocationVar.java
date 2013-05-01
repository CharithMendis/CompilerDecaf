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
public class ASLocationVar extends ASLocation{


    public ASLocationVar(String name) {
        this.name = name;
        this.load = null;
        this.store = null;
        this.isStore = false;
    }
    
    public ASLocationVar(String name,int line,int col){
        this(name);
        this.column = col;
        this.line = line;
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

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
public class ASLocationArray extends ASLocation{
    public ASExpr location;

    public ASLocationArray(String name,ASExpr location) {
        this.name = name;
        this.location = location;
        this.load = null;
        this.store = null;
        this.isStore = false;
    }
    
    public ASLocationArray(String name,ASExpr location, int line, int col){
        this(name, location);
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

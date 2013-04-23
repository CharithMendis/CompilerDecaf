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
public class ASFor extends ASStatement{
    
    public ASLocationVar var;
    public ASExpr startExpr;
    public ASExpr endExpr;
    public ASBlock block;

    public ASFor(String name,ASExpr startExpr, ASExpr endExpr, ASBlock block) {
        this.startExpr = startExpr;
        this.endExpr = endExpr;
        this.block = block;
        this.var =  new ASLocationVar(name);
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        ASUtilities.visit(var, t, v);
        ASUtilities.visit(startExpr, t, v);
        ASUtilities.visit(endExpr, t, v);
        ASUtilities.visit(block, t, v);
    }
    
    
}

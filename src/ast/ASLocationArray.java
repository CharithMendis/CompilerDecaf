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
public class ASLocationArray extends ASLocation{
    public ASExpr location;

    public ASLocationArray(String name,ASExpr location) {
        this.name = name;
        this.location = location;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        ASUtilities.visit(location, t, v);
    }
    
}

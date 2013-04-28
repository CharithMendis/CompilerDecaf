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
public class ASIf extends ASStatement{
    
    //( ASExpr (if) , ASBlock (if block) , ASBlock (else block))
    public ASExpr condition;
    public ASBlock ifstat;
    public ASBlock elsestat;
    public boolean elsePresent;

    

    public ASIf(ASExpr condition, ASBlock ifstat, ASBlock elsestat) {
        this.condition = condition;
        this.ifstat = ifstat;
        this.elsestat = elsestat;
        if(elsestat==null)
            elsePresent = false;
        else
            elsePresent = true;
    }
    
    public ASIf(ASExpr condition, ASBlock ifstat, ASBlock elsestat, int line, int col){
        this(condition, ifstat, elsestat);
        this.line = line;
        this.column = col;
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        ASUtilities.visit(condition, t, v);
        ASUtilities.visit(ifstat, t, v);
        if(elsePresent)
            ASUtilities.visit(elsestat, t, v);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
}

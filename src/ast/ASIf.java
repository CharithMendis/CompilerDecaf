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

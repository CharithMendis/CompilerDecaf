/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import ast.ASBinaryExpr;
import codegen.VisitorIR;

/**
 *
 * @author Charith
 */
public class IRLConEx extends IRLBoolEx{
      
    

    public IRLConEx(String stringop, IRLEx lhs, IRLEx rhs) {
        super(stringop, lhs, rhs);
        isStored = false;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }

    @Override
    public void store() {
        isStored = true;
        this.trueLabel = new IRLLabel();
        this.falseLabel = new IRLLabel();
        this.jumpAfterFalse = new IRLLabel();
        this.falseEx = this.falseLabel;
        this.trueEx = null;
    }
    
    
}

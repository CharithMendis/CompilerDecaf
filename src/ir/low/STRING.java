/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import codegen.VisitorIR;

/**
 *
 * @author Charith
 */
public class STRING extends IRLEx{
    
    public IRLLabel label;
    public String value;

    public STRING(IRLLabel label, String value) {
        this.label = label;
        this.value = value;
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import codegen.VisitorIR;
import semantic.symbol.MethodDescriptor;

/**
 *
 * @author Charith
 */
public class IRLActivation extends IRL{
    
    public STRING methodName;
    
    public IRLLabel name;
    public int localSize;
    public IRLTerStm head;
    public MethodDescriptor mdes;
    
    public String code;
    
    
    public IRLActivation(String name,MethodDescriptor mdes){
        this.mdes = mdes;
        this.localSize = 0;
        this.name = new IRLLabel("_" + name);
        this.head = new IRLTerStm();
        this.code = "";
        this.methodName = new STRING(null,"\"\\\"" + name + "\\\"\"");
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
}

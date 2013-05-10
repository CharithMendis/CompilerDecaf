/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import codegen.VisitorIR;
import java.util.ArrayList;
import semantic.symbol.MethodDescriptor;

/**
 *
 * @author Charith
 */
public class CALL extends IRLEx{
    
    public IRLLabel name;
    public ArrayList<IRLEx> arguments;
    
    
    

    public CALL(IRLLabel name) {
        this.name = name;
        arguments = new ArrayList();
    }
    
    public void addArgument(IRLEx ex){
        arguments.add(ex);
    }
    
    public IRLEx getArgument(int i){
        return arguments.get(i);
    }
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic.symbol;

import ast.ASType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Charith
 */
public class MethodDescriptor extends Descriptor{
    
    //I am using the same type class as that in the ast.
    public ASType returnValue;
    public String name;
    public ArrayList<VariableDescriptor> parameters;
    public Environment env;   //has an environment

    public MethodDescriptor(ASType returnValue, String name,Environment current) {
        this.returnValue = returnValue;
        this.name = name;
        parameters = new ArrayList();
        env = new Environment(current);
        
        
    }
    
    public void addToEnvironment(HashMap<String,Descriptor> map){
        env.symbolTable.putAll(map);
    }

    @Override
    public void print() {
        System.out.println("method: [return type] " + returnValue.stringType + " [name] " + name);
    }
    
    
    
    
   
}

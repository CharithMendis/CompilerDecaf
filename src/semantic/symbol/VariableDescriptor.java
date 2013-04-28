/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic.symbol;

import ast.ASType;

/**
 *
 * @author Charith
 */
public class VariableDescriptor extends Descriptor{
    
    public ASType type;
    public String name;

    public VariableDescriptor(ASType type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println("variable: [type] " + type.stringType + " [name] " + name);
    }
    
    
    
}

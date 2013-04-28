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

    public VariableDescriptor(ASType type, String name,int line,int col) {
        this.type = type;
        this.name = name;
        this.line = line;
        this.column = col;
    }

    @Override
    public void print() {
        System.out.println("variable: [type] " + type.stringType + " [name] " + name);
    }
    
    
    
}

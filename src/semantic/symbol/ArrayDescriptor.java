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
public class ArrayDescriptor extends Descriptor{
    
    public ASType type;
    public String name;
    public int size;

    public ArrayDescriptor(ASType type, String name,int size) {
        this.type = type;
        this.name = name;
        this.size = size;
    }
    
    @Override
    public void print() {
        System.out.println("variable: [type] " + type.stringType + " [name] " + name + " [size] " + size);
    }
    
}

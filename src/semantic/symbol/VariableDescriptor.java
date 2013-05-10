/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic.symbol;

import ast.ASType;
import ir.low.IRLTemp;

/**
 *
 * @author Charith
 */
public class VariableDescriptor extends FieldDescriptor{
    
    public int kind;   //where is this used????? - in the method local size allocation (parameters are already caller assigned)
    
    public static final int FIELD = 1;
    public static final int PARA  = 2;
    public static final int LOCAL = 3;

    public VariableDescriptor(ASType type, String name,int line,int col) {
        this.type = type;
        this.name = name;
        this.line = line;
        this.column = col;
        this.kind = LOCAL;
    }

    @Override
    public void print() {
        System.out.println("variable: [type] " + type.stringType + " [name] " + name);
    }
    
    
    
}

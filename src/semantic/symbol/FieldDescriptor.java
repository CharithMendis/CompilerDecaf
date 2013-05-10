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
public abstract class FieldDescriptor extends Descriptor{
    
    public ASType type;
    public IRLTemp loc; //this stores the location of the field descriptor
    
}

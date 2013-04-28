/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;
import semantic.Visitor;
import semantic.VisitorWithReturn;

/**
 *
 * @author Charith
 */
public class ASLibraryCall extends ASMethodCall{
    
    public ArrayList<ASCalloutArg> arguments;

    public ASLibraryCall(String name) {
        arguments = new ArrayList();
        this.name = name;
    }
    
    public ASLibraryCall(String name, int line, int col){
        this(name);
        this.line = line;
        this.column = col;
    }
    
    public void addArgument(ASCalloutArg arg){
        arguments.add(arg);
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
    
    
}

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
public class ASProgram extends AS{
    
    public ArrayList<ASMethodDecl> methods;
    public ArrayList<ASFieldDecl> fields;

    public ASProgram() {
        methods = new ArrayList<ASMethodDecl>();
        fields = new ArrayList<ASFieldDecl>();
    }
    
    public ASProgram(int line, int col){
        this();
        this.line = line;
        this.column = col;
    }
    
    public void addMethodDecl(ASMethodDecl method){
        methods.add(method);
    }
    
    public void addFieldDecl(ASFieldDecl field){
        fields.add(field);
    }
    
    public void accept(Visitor v, int t){
        v.visit(this, t);
        
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
   
    
}

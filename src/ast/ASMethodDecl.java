/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;
import semantic.Visitor;
import semantic.VisitorWithPara;
import semantic.VisitorWithReturn;

/**
 *
 * @author Charith
 */
public class ASMethodDecl extends AS{

    public ASType type;
    public String name;
    public ArrayList<ASVariable> parameters;
    public ASBlock block;

    public ASMethodDecl(ASType type, String name) {
        this.type = type;
        this.name = name;
        parameters = new ArrayList<ASVariable>();
    }
    
    public ASMethodDecl(ASType type, String name,int line,int col){
        this(type,name);
        this.line = line;
        this.column = col;
    }
    
    public void addParameters(ASVariable var){
        parameters.add(var);
    }
    
    public void addBlock(ASBlock block){
        this.block = block;
    }   
    
    public void accept(VisitorWithPara v, int t) {
        v.visit(this,t);
        
    }
    
    public void accept(Visitor v) {
        v.visit(this);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
}

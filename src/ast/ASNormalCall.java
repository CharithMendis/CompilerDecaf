/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import ir.high.IRMethod;
import java.util.ArrayList;
import semantic.Visitor;
import semantic.VisitorWithPara;
import semantic.VisitorWithReturn;

/**
 *
 * @author Charith
 */
public class ASNormalCall extends ASMethodCall{
    
    public ArrayList<ASExpr> arguments;
    public IRMethod method;

    public ASNormalCall(String name) {
        arguments = new ArrayList();
        this.name = name;
        this.method = null;
    }
    
    public ASNormalCall(String name, int line,int col){
        this(name);
        this.line = line;
        this.column = col;
    } 
    
    public void addArgument(ASExpr expr){
        arguments.add(expr);
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

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
    
    public void addParameters(ASVariable var){
        parameters.add(var);
    }
    
    public void addBlock(ASBlock block){
        this.block = block;
    }   
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        for(int i=0;i<parameters.size();i++){
            ASUtilities.visit(parameters.get(i), t, v);
        }
        ASUtilities.visit(block, t, v);
    }
    
    @Override
    public Object acceptWithReturn(VisitorWithReturn v) {
        return v.visit(this);
    }
    
}

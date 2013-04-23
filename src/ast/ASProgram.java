/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;
import semantic.Visitor;

/**
 *
 * @author Charith
 */
public class ASProgram extends AS{
    
    private ArrayList<ASMethodDecl> methods;
    private ArrayList<ASFieldDecl> fields;

    public ASProgram() {
        methods = new ArrayList<ASMethodDecl>();
        fields = new ArrayList<ASFieldDecl>();
    }
    
    public void addMethodDecl(ASMethodDecl method){
        methods.add(method);
    }
    
    public void addFieldDecl(ASFieldDecl field){
        fields.add(field);
    }
    
    public void accept(Visitor v, int t){
        v.visit(this, t);
        for(int i=0;i<fields.size();i++){
            ASUtilities.visit(fields.get(i), t, v);
        }
        for(int i=0;i<methods.size();i++){
            ASUtilities.visit(methods.get(i), t, v);
        }
    }
    
    
   
    
}

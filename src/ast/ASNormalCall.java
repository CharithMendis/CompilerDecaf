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
public class ASNormalCall extends ASMethodCall{
    
    public ArrayList<ASExpr> arguments;

    public ASNormalCall(String name) {
        arguments = new ArrayList();
        this.name = name;
    }
    
    public void addArgument(ASExpr expr){
        arguments.add(expr);
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        for(int i=0;i<arguments.size();i++){
            ASUtilities.visit(arguments.get(i), t, v);
        }
    }
    
    
    
}

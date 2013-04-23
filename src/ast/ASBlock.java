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
public class ASBlock extends ASStatement{
    //ASVariable/s , ASStatement/s and ASBlock/s
    public ArrayList<ASVariable> var;
    public ArrayList<ASStatement> statements;

    public ASBlock() {
        var = new ArrayList();
        statements = new ArrayList();
        
    }

    public void addVar(ASVariable v){
        var.add(v);
    }
    
    public void addStatement(ASStatement s){
        statements.add(s);
    }
    
    public void accept(Visitor v, int t) {
        v.visit(this,t);
        for(int i=0;i<var.size();i++){
            ASUtilities.visit(var.get(i), t, v);
        }
        for(int i=0;i<statements.size();i++){
            ASUtilities.visit(statements.get(i), t, v);
        }
    }

}

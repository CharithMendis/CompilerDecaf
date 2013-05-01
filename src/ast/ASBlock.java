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
public class ASBlock extends ASStatement{
    //ASVariable/s , ASStatement/s and ASBlock/s
    public ArrayList<ASVariable> var;
    public ArrayList<ASStatement> statements;

    public ASBlock() {
        var = new ArrayList();
        statements = new ArrayList();
        
    }
    
    public ASBlock(int line, int col){
        this();
        this.line = line;
        this.column = col;
    }

    public void addVar(ASVariable v){
        var.add(v);
    }
    
    public void addStatement(ASStatement s){
        statements.add(s);
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

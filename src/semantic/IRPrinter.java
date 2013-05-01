/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic;

import ast.AS;
import ast.ASArray;
import ast.ASAssignment;
import ast.ASBinaryExpr;
import ast.ASBlock;
import ast.ASBooleanLiteral;
import ast.ASBreak;
import ast.ASCharLiteral;
import ast.ASContinue;
import ast.ASFor;
import ast.ASIf;
import ast.ASIntLiteral;
import ast.ASLibraryCall;
import ast.ASLocationArray;
import ast.ASLocationVar;
import ast.ASMethodCallE;
import ast.ASMethodCallS;
import ast.ASMethodDecl;
import ast.ASNormalCall;
import ast.ASProgram;
import ast.ASReturn;
import ast.ASStringLiteral;
import ast.ASUnaryExpr;
import ast.ASVariable;

/**
 *
 * @author Charith
 */
public class IRPrinter implements VisitorWithPara{
    
    public static void accept(AS val,int t,VisitorWithPara v){
        Class c = val.getClass();
        c.cast(val);
        val.accept(v, t+1);
    }
    
    public void keepTab(int t){
        for(int i=0;i<t;i++){
            System.out.print(" ");
        }
    }

    @Override
    public void visit(ASProgram p, int t) {
        for(int i=0;i<p.methods.size();i++){
            accept(p.methods.get(i), t, this);
        }
    }

    @Override
    public void visit(ASMethodDecl m, int t) {
        System.out.println("method_decl: " + m.type.stringType + " " + m.name);
        accept(m.block, t, this);
    }

    @Override
    public void visit(ASVariable var, int t) {
        //no need to print
    }

    @Override
    public void visit(ASArray array, int t) {
        //no need to print
    }

    @Override
    public void visit(ASAssignment assign, int t) {
        keepTab(t);
        System.out.println("assign: " + assign.stringop);
        accept(assign.location, t, this);
        accept(assign.expr, t, this);
    }

    @Override
    public void visit(ASBreak b, int t) {
        keepTab(t);
        System.out.println("break:");
    }

    @Override
    public void visit(ASContinue c, int t) {
        keepTab(t);
        System.out.println("continue:");
    }

    @Override
    public void visit(ASFor f, int t) {
        keepTab(t);
        System.out.println("for:");
        //accept(f.var, t, this);
        keepTab(t);
        System.out.println("start: ");
        accept(f.startExpr, t, this);
        keepTab(t);
        System.out.println("end: ");
        accept(f.endExpr, t, this);
        accept(f.block, t, this);
    }

    @Override
    public void visit(ASIf f, int t) {
        keepTab(t);
        System.out.println("if:");
        keepTab(t);
        System.out.println("condition: ");
        accept(f.condition, t, this);
        keepTab(t);
        System.out.println("if ");
        accept(f.ifstat, t, this);
        if(f.elsePresent){
            keepTab(t);
            System.out.println("else ");
            accept(f.elsestat, t, this);
        }
    }

    @Override
    public void visit(ASMethodCallS call, int t) {
        keepTab(t);
        System.out.println("method call");
        accept(call.method, t, this);
    }

    @Override
    public void visit(ASReturn ret, int t) {
        keepTab(t);
        System.out.println("Return: ");
        if(ret.returnExpr != null)
           accept(ret.returnExpr, t, this);
    }

    @Override
    public void visit(ASBinaryExpr ex, int t) {
        keepTab(t);
        System.out.println("BinaryOp: " + ex.stringop);
        accept(ex.lhs, t, this);
        accept(ex.rhs, t, this);
    }

    @Override
    public void visit(ASBooleanLiteral b, int t) {
        keepTab(t);
        System.out.println("BooleanLiteral: " + b.bool);
    }

    @Override
    public void visit(ASCharLiteral c, int t) {
        keepTab(t);
        System.out.println("CharLiteral: " + c.character);
    }

    @Override
    public void visit(ASIntLiteral i, int t) {
        keepTab(t);
        System.out.println("IntLiteral: " + i.integer);
    }

    @Override
    public void visit(ASLocationArray var, int t) {
        keepTab(t);
        if(var.isStore){
            if(var.store != null) {
                System.out.println("Store node: [" + var.store.fdes.type.stringType + "] " + var.name  + " " + var.store.fdes.line + ":" 
                        + var.store.fdes.column + " ");
            }
            else {
                System.out.println("Store node: [" + var.store + "] " + var.name);
            }
        }
        else{
            if(var.load != null) {
                System.out.println("Load node: [" + var.load.fdes.type.stringType + "] " + var.name  + " " + var.load.fdes.line + ":" 
                        + var.load.fdes.column + " ");
            }
            else {
                System.out.println("Load node: [" + var.load + "] " + var.name);
            }
        }
        keepTab(t+1);
        System.out.println("Expression:");
        accept(var.location, t+1, this);
    }

    @Override
    public void visit(ASLocationVar var, int t) {
        keepTab(t);
        if(var.isStore){
            if(var.store != null) {
                System.out.println("Store node: [" + var.store.fdes.type.stringType + "] " + var.name  + " " + var.store.fdes.line + ":" 
                        + var.store.fdes.column + " ");
            }
            else {
                System.out.println("Store node: [" + var.store + "] " + var.name);
            }
        }
        else{
            if(var.load != null) {
                System.out.println("Load node: [" + var.load.fdes.type.stringType + "] " + var.name  + " " + var.load.fdes.line + ":" 
                        + var.load.fdes.column + " ");
            }
            else {
                System.out.println("Load node: [" + var.load + "] " + var.name);
            }
        }
    }
    

    @Override
    public void visit(ASMethodCallE call, int t) {
        keepTab(t);
        System.out.println("method call");
        accept(call.method, t, this);
    }

    @Override
    public void visit(ASStringLiteral l, int t) {
        keepTab(t);
        System.out.println("StringLiteral: " + l.argument);
    }

    @Override
    public void visit(ASUnaryExpr ex, int t) {
        keepTab(t);
        System.out.println("UnaryExpr: " + ex.stringop);
        accept(ex.expr, t, this);
    }

    @Override
    public void visit(ASLibraryCall m, int t) {
        keepTab(t);
        System.out.println("LibraryCall: " + m.name);
        for(int i=0;i<m.arguments.size();i++){
            accept(m.arguments.get(i), t, this);
        }
    }

    @Override
    public void visit(ASNormalCall l, int t) {
        keepTab(t);
        System.out.print("NormalCall: " );
        if(l.method != null){
            System.out.println(l.name + " [" + l.method.mdes.returnValue.stringType + "] " + l.method.mdes.line + ":" 
                    + l.method.mdes.column);
        }
        for(int i=0;i<l.arguments.size();i++){
            accept(l.arguments.get(i), t, this);
        }
    }

    @Override
    public void visit(ASBlock block, int t) {
        keepTab(t);
        System.out.println("block:");
        for(int i=0;i<block.statements.size();i++){
            accept(block.statements.get(i), t, this);
        }
    }
    
}

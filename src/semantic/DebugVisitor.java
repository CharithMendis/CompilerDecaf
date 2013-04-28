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
public class DebugVisitor implements Visitor{
    
    
    public static void accept(AS val,int t,Visitor v){
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
        keepTab(t);
        System.out.println("program");
    }

    @Override
    public void visit(ASMethodDecl m, int t) {
        keepTab(t);
        System.out.println("method_decl: " + m.type.stringType + " " + m.name);
    }

    @Override
    public void visit(ASVariable var, int t) {
        keepTab(t);
        System.out.println("var: " + var.type.stringType + " " + var.name);
    }

    @Override
    public void visit(ASArray array, int t) {
        keepTab(t);
        System.out.println("var: " + array.type.stringType + " " + array.name + "[" + array.size + "]");
    }

    @Override
    public void visit(ASAssignment assign, int t) {
        keepTab(t);
        System.out.println("assign: " + assign.stringop);
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
    }

    @Override
    public void visit(ASIf f, int t) {
        keepTab(t);
        System.out.println("if:");
    }

    @Override
    public void visit(ASMethodCallS call, int t) {
        keepTab(t);
        System.out.println("method call");
    }

    @Override
    public void visit(ASReturn ret, int t) {
        keepTab(t);
        System.out.println("Return: ");
    }

    @Override
    public void visit(ASBinaryExpr ex, int t) {
        keepTab(t);
        System.out.println("BinaryOp: " + ex.stringop);
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
    public void visit(ASLocationArray array, int t) {
        keepTab(t);
        System.out.println("ArrayLocation: " + array.name);
    }

    @Override
    public void visit(ASLocationVar var, int t) {
        keepTab(t);
        System.out.println("VarLocation: " + var.name);
    }

    @Override
    public void visit(ASMethodCallE call, int t) {
        keepTab(t);
        System.out.println("method call");
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
    }

    @Override
    public void visit(ASLibraryCall m, int t) {
        keepTab(t);
        System.out.println("LibraryCall: " + m.name);
    }

    @Override
    public void visit(ASNormalCall l, int t) {
        keepTab(t);
        System.out.println("NormalCall: " + l.name);
    }

    @Override
    public void visit(ASBlock block, int t) {
        keepTab(t);
        System.out.println("block:");
    }

    
    
}

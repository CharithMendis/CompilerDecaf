/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic;

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
public interface Visitor {
    //program and decl
    public void visit(ASProgram p, int t);
    public void visit(ASMethodDecl m,int t);
    //FieldDecl
    public void visit(ASVariable var,int t);
    public void visit(ASArray array,int t);
    //statements
    public void visit(ASAssignment assign,int t);
    public void visit(ASBreak b,int t);
    public void visit(ASContinue c,int t);
    public void visit(ASFor f,int t);
    public void visit(ASIf f,int t);
    public void visit(ASMethodCallS call,int t);
    public void visit(ASReturn ret,int t);
    //callout arg + expr
    public void visit(ASBinaryExpr ex,int t);
    public void visit(ASBooleanLiteral b,int t);
    public void visit(ASCharLiteral c,int t);
    public void visit(ASIntLiteral i,int t);
    public void visit(ASLocationArray array,int t);
    public void visit(ASLocationVar var,int t);
    public void visit(ASMethodCallE call,int t);
    public void visit(ASStringLiteral l,int t);
    public void visit(ASUnaryExpr ex,int t);
    //method call
    public void visit(ASLibraryCall m,int t);
    public void visit(ASNormalCall l,int t);
    public void visit(ASBlock block,int t);
    
}

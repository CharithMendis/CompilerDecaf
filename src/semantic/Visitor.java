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
    public void visit(ASProgram p);
    public void visit(ASMethodDecl m);
    //FieldDecl
    public void visit(ASVariable var);
    public void visit(ASArray array);
    //statements
    public void visit(ASAssignment assign);
    public void visit(ASBreak b);
    public void visit(ASContinue c);
    public void visit(ASFor f);
    public void visit(ASIf f);
    public void visit(ASMethodCallS call);
    public void visit(ASReturn ret);
    //callout arg + expr
    public void visit(ASBinaryExpr ex);
    public void visit(ASBooleanLiteral b);
    public void visit(ASCharLiteral c);
    public void visit(ASIntLiteral i);
    public void visit(ASLocationArray array);
    public void visit(ASLocationVar var);
    public void visit(ASMethodCallE call);
    public void visit(ASStringLiteral l);
    public void visit(ASUnaryExpr ex);
    //method call
    public void visit(ASLibraryCall m);
    public void visit(ASNormalCall l);
    public void visit(ASBlock block);
    
}

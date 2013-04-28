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
public interface VisitorWithReturn {
    public Object visit(ASProgram p);
    public Object visit(ASMethodDecl m);
    //FieldDecl
    public Object visit(ASVariable var);
    public Object visit(ASArray array);
    //statements
    public Object visit(ASAssignment assign);
    public Object visit(ASBreak b);
    public Object visit(ASContinue c);
    public Object visit(ASFor f);
    public Object visit(ASIf f);
    public Object visit(ASMethodCallS call);
    public Object visit(ASReturn ret);
    //callout arg + expr
    public Object visit(ASBinaryExpr ex);
    public Object visit(ASBooleanLiteral b);
    public Object visit(ASCharLiteral c);
    public Object visit(ASIntLiteral i);
    public Object visit(ASLocationArray array);
    public Object visit(ASLocationVar var);
    public Object visit(ASMethodCallE call);
    public Object visit(ASStringLiteral l);
    public Object visit(ASUnaryExpr ex);
    //method call
    public Object visit(ASLibraryCall m);
    public Object visit(ASNormalCall l);
    public Object visit(ASBlock block);
}

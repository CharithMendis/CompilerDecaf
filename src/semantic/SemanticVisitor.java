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
import ast.ASFieldDecl;
import ast.ASFor;
import ast.ASIf;
import ast.ASIntLiteral;
import ast.ASLibraryCall;
import ast.ASLocationArray;
import ast.ASLocationVar;
import ast.ASMethodCall;
import ast.ASMethodCallE;
import ast.ASMethodCallS;
import ast.ASMethodDecl;
import ast.ASNormalCall;
import ast.ASProgram;
import ast.ASReturn;
import ast.ASStatement;
import ast.ASStringLiteral;
import ast.ASType;
import ast.ASUnaryExpr;
import ast.ASVariable;
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.Descriptor;
import semantic.symbol.Environment;
import semantic.symbol.MethodDescriptor;
import semantic.symbol.VariableDescriptor;

/**
 *
 * @author Charith
 */

//limitations - 
//no two methods can have same names - overrided methods not allowed
//field and method cannot have the same name = this can be remedied in next stages.
//callout methods cannot be used in exprs.

//need to check return statement
//casting in ASMethodCallE

public class SemanticVisitor implements VisitorWithReturn{

    public Environment top;
    public Environment current;
    public SemanticErrorPrint error;
    
    public int noOfErrors;
    
    private boolean newMethod;
    private MethodDescriptor currentMethod;
    private boolean isFor;
    
    public String filename;
    
    public SemanticVisitor(String filename){
        this.newMethod = false;
        error = new SemanticErrorPrint(filename);
        noOfErrors = 0;
        currentMethod = null;
        isFor = false;
        this.filename = filename;
    }
    
    @Override
    public Object visit(ASProgram p) {
        
        top = new Environment(null);
        current = top;
        
        for(int i=0;i<p.fields.size();i++){
            ASFieldDecl f = p.fields.get(i);
            //casting
            Class c = f.getClass();
            c.cast(f);
            f.acceptWithReturn(this);
        }
        
        for(int i=0;i<p.methods.size();i++){
            p.methods.get(i).acceptWithReturn(this);
        }
        
        //Place to check the main method is there
        MethodDescriptor mdes = (MethodDescriptor)current.get("main");
        //ERROR
        if(mdes == null){
            //CHECK
            error.printNotDeclared("main",SemanticErrorPrint.METHOD,p.line,p.column);
        }
        else{
            //ERROR
            if(!mdes.parameters.isEmpty()){
                error.printMainError(mdes.line,mdes.column);
            }
        }
        
        //ideally raise an exception
        if(noOfErrors!=0){
            System.out.println("Semantic Errors: "+ noOfErrors);
        }
        
        return null;
        
    }

    @Override
    public Object visit(ASMethodDecl m) {
        
        //create an descriptor
        MethodDescriptor mdes = new MethodDescriptor(m.type,m.name,current,m.line,m.column);
        currentMethod = mdes;
        
        
        //parameter environment - just to get the symbol table not an explicit environment
        Environment parameters = new Environment(current);
        current = parameters;
        
        //put the parameter values
        for(int i=0;i<m.parameters.size();i++){
            //visit the parameters within the method
            VariableDescriptor var = (VariableDescriptor)m.parameters.get(i).acceptWithReturn(this);
            if(var!=null){
                mdes.parameters.add(var);
            }
            
        }
        
        mdes.addToEnvironment(parameters.symbolTable);
        
        current = current.prev; //back to the top
        
        current = mdes.env;  //current is the environment inside method descriptor
        newMethod = true;
        
        //traverse the block with in the method decl
        m.block.acceptWithReturn(this);  //block returns to the top
        

        //ERROR
        if(current.lookup(m.name)){
            error.printDuplicateError(m.name,SemanticErrorPrint.METHOD,m.line,m.column);
            noOfErrors++;
        }
        else{
            current.put(m.name, mdes);
            
        }
        
        currentMethod = null;
        
        return null;
        
    }

    @Override
    public Object visit(ASVariable var) {
        VariableDescriptor des = new VariableDescriptor(var.type,var.name,var.line,var.column);
        
        //ERROR
        if(current.lookup(var.name)){
            error.printDuplicateError(var.name,SemanticErrorPrint.VARIABLE,var.line,var.column);
            noOfErrors++;
            return null;
        }
        else{
            current.put(var.name, des);
            return des;
        }
        
    }

    @Override
    public Object visit(ASArray array) {
        
        //ERROR
        if(array.size == 0) {
            error.printArrayInitError(array.name,array.line,array.column);
            noOfErrors++;
        }
        
        
        ArrayDescriptor des = new ArrayDescriptor(array.type,array.name,array.size,array.line,array.column);
        
        //ERROR
        if(current.lookup(array.name)){
            error.printDuplicateError(array.name,SemanticErrorPrint.VARIABLE,array.line,array.column);
            noOfErrors++;
        }
        else{
            current.put(array.name, des);
        }
        return null;
    }
    
    @Override
    public Object visit(ASBlock block) {
        
        if(newMethod){ //no need to create a new environment
            newMethod = false;
        }
        else{ 
            Environment en = new Environment(current);
            current.addEnv(en);
            current = en;
        }
        
        //traverse the variables
        for(int i=0;i<block.var.size();i++){
            ASVariable var = block.var.get(i);
            var.acceptWithReturn(this);
        }
        
        //traverse the statements
        for(int i=0;i<block.statements.size();i++){
            ASStatement f = block.statements.get(i);
            f.acceptWithReturn(this);
        }
        
        current = current.prev;  //end environment
        
        return null;
        
    }

    @Override
    public Object visit(ASAssignment assign) {
        
        ASType lhs = (ASType)assign.location.acceptWithReturn(this);
        ASType rhs = (ASType)assign.expr.acceptWithReturn(this);
        
        //must have same type
        
        if(assign.operator == ASAssignment.ASSIGN){
            //ERROR
            if(lhs.type != rhs.type){
                error.printTypeErrorVariable(lhs.stringType, rhs.stringType, assign.stringop,assign.line,assign.column);
            }
        }
        else{
            //ERROR
            if(lhs.type != ASType.INT || rhs.type != ASType.INT){
                error.printTypeErrorVariable(lhs.stringType, rhs.stringType, assign.stringop,assign.line,assign.column);
            }
        }
        
        return null;
    }

    @Override
    public Object visit(ASBreak b) {
        
        //ERROR
        if(!isFor){
            error.printBreakOrContError("break",b.line,b.column);
        }
        
        return null;
    }

    @Override
    public Object visit(ASContinue c) {
        
        //ERROR
        if(!isFor){
            error.printBreakOrContError("continue",c.line,c.column);
        }
        
        
        return null;
    }

    @Override
    public Object visit(ASFor f) {
        isFor = true;
        ASType start = (ASType)f.startExpr.acceptWithReturn(this);
        ASType end = (ASType)f.endExpr.acceptWithReturn(this);
        
        //ERROR
        if(start.type != ASType.INT || end.type != ASType.INT){
            error.printForError(start.stringType, end.stringType,f.line,f.column);
        }
        
        f.block.acceptWithReturn(this);
        isFor = false;
        
        return null;
        
    }

    @Override
    public Object visit(ASIf f) {
        
        ASType t = (ASType)f.condition.acceptWithReturn(this);
        
        //must be of boolean type
        //ERROR
        if(t.type != ASType.BOOLEAN){
            error.printIfError(t.stringType,f.line,f.column);
        }
        f.ifstat.acceptWithReturn(this);
        if(f.elsePresent){
            f.elsestat.acceptWithReturn(this);
        }
        
        return null;
        
    }

    

    @Override
    public Object visit(ASReturn ret) {
        if(currentMethod.returnValue.type == ASType.VOID){  // no expression needed
            //ERROR cannot return a value
            if(ret.returnExpr != null){
                error.printNoReturnError(currentMethod.name,ret.line,ret.column);
            }
        }
        else{
            if(ret.returnExpr == null){
                error.printReturnTypeError(currentMethod.name, currentMethod.returnValue.stringType,ret.line,ret.column);
            }
            else{
                ASType t = (ASType)ret.returnExpr.acceptWithReturn(this);
                if(t.type != currentMethod.returnValue.type){
                    error.printReturnTypeError(currentMethod.name, currentMethod.returnValue.stringType,ret.line,ret.column);
                }
            }
        }
        
        return null;
    }
    
    @Override
    public Object visit(ASBinaryExpr ex) {
        
        ASType lhs = (ASType)ex.lhs.acceptWithReturn(this);
        ASType rhs = (ASType)ex.rhs.acceptWithReturn(this);
        
        if(ex.operator==ASBinaryExpr.MULT ||
           ex.operator==ASBinaryExpr.PLUS ||
           ex.operator==ASBinaryExpr.DIV  ||
           ex.operator==ASBinaryExpr.MINUS ||
           ex.operator==ASBinaryExpr.MOD ||
           ex.operator==ASBinaryExpr.GE ||
           ex.operator==ASBinaryExpr.GT ||
           ex.operator==ASBinaryExpr.LE ||
           ex.operator==ASBinaryExpr.LT ){   //arithmetic operations && relational operations
            //ERROR
            if(lhs.type != ASType.INT || rhs.type != ASType.INT){
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                return new ASType("error");
            }
            
        }
        else if(ex.operator == ASBinaryExpr.EQ || ex.operator == ASBinaryExpr.NEQ){
            if(lhs.type != rhs.type){
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                return new ASType("error");
            }
        }
        else{
            if(lhs.type != ASType.BOOLEAN || rhs.type != ASType.BOOLEAN){
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                return new ASType("error");
            }
        }
        
        return lhs;
    }
    
    @Override
    public Object visit(ASUnaryExpr ex) {
        ASType t = (ASType)ex.expr.acceptWithReturn(this);
        if(ex.operator == ASUnaryExpr.MINUS){  //should be of type int
            //ERROR
            if(t.type != ASType.INT){
                error.printTypeErrorVariable("int", t.stringType,ex.line,ex.column);
                noOfErrors++;
                return new ASType("error");
            }
            else{
                return t;
            }
        }
        else{  //should be of type boolean
            //ERROR
            if(t.type != ASType.BOOLEAN){
                error.printTypeErrorVariable("boolean", t.stringType,ex.line,ex.column);
                noOfErrors++;
                return new ASType("error");
            }
            else{
                return t;
            }
        }
    }

    @Override
    public Object visit(ASBooleanLiteral b) {
        
        return new ASType("boolean");
    }

    @Override
    public Object visit(ASCharLiteral c) {
        return new ASType("int");   //characters are considered integers
    }

    @Override
    public Object visit(ASIntLiteral i) {
        return new ASType("int");
    }

    @Override
    public Object visit(ASLocationArray array) {
        
        Descriptor des = current.get(array.name);

        //ERROR
        if(des == null){
            error.printNotDeclared(array.name, SemanticErrorPrint.VARIABLE,array.line,array.column);
        }
        else{
            
            //ERROR
            if(des.getClass() != ArrayDescriptor.class){
                error.printArrayTypeError(array.name,array.line,array.column);
            }
            else{
                ArrayDescriptor ades = (ArrayDescriptor)des;
                ASType t = (ASType)array.location.acceptWithReturn(this);
                //ERROR
                if(t.type != ASType.INT){
                    error.printArrayLocationError(array.name,array.line,array.column);
                }
                else{
                    return ades.type.element;
                }
            }
        }
            
        
        return new ASType("error");
    }

    @Override
    public Object visit(ASLocationVar var) {
        
        VariableDescriptor des = (VariableDescriptor)current.get(var.name);
        
        //ERROR
        if(des == null){
            error.printNotDeclared(var.name, SemanticErrorPrint.VARIABLE,var.line,var.column);
        }
        else{
            return des.type;
        }
        return new ASType("error");
    }
    
    @Override
    public Object visit(ASMethodCallS call) {
        //can be a normal or a library call
        ASType t = (ASType)call.method.acceptWithReturn(this);
        
        //no need to check the return value here
        return t;
    }

    @Override
    public Object visit(ASMethodCallE call) {
        
        //can do a further check to see whether this is a normal call
        ASNormalCall asmethod = (ASNormalCall)call.method;
        
        ASType t = (ASType)asmethod.acceptWithReturn(this);
        
        //this should return a value
        //ERROR
        if(t.type == ASType.VOID){
            error.printNoReturnVal(asmethod.name, SemanticErrorPrint.METHOD,call.line,call.column);
            noOfErrors++;
        }
        return t;

    }

    @Override
    public Object visit(ASStringLiteral l) {
        return null;
    }

   

    @Override
    public Object visit(ASLibraryCall m) {
        //no semantics
        return null;
    }

    @Override
    public Object visit(ASNormalCall asmethod) {
        //first check whether the method exists
        MethodDescriptor mdes = (MethodDescriptor)current.get(asmethod.name);
        //ERROR
        if(mdes == null){  //no such method
            error.printNotDeclared(asmethod.name,SemanticErrorPrint.METHOD,asmethod.line,asmethod.column);
            noOfErrors++;
            return new ASType("error");
        }
        else{
            //now need to check for the expr types as well as the amount
            //check whether enough parameters are there
            //ERROR
            if(asmethod.arguments.size() != mdes.parameters.size()){
                error.printNotEnoughParameters(mdes.name,asmethod.line,asmethod.column);
                noOfErrors++;
            }
            else{
                //type check
                for(int i=0;i<asmethod.arguments.size();i++){
                    ASType t = (ASType)asmethod.arguments.get(i).acceptWithReturn(this);
                    //need the parameters in order
                    VariableDescriptor vdes = mdes.parameters.get(i);

                    //ERROR
                    if(vdes.type.type != t.type){
                        error.printTypeErrorMethod(mdes.name, vdes.name, SemanticErrorPrint.METHOD, vdes.type.stringType,vdes.line,vdes.column);
                        noOfErrors++;
                        break;
                    }
                }
            }
            return mdes.returnValue;   
        }
        
    }
 

    
    
}

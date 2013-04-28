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

//need to check return statement
//error - means it is being dealt somewhere / NA - means a callout give a warning

public class SemanticVisitor implements VisitorWithReturn{

    public Environment top;
    public Environment current;
    public SemanticErrorPrint error;
    
    public int noOfErrors;
    public boolean warning;
    
    private boolean newMethod;
    private MethodDescriptor currentMethod;
    private boolean isFor;
    public String filename;
    
    public SemanticVisitor(String filename,boolean warning){
        this.newMethod = false;
        error = new SemanticErrorPrint(filename);
        noOfErrors = 0;
        currentMethod = null;
        isFor = false;
        this.filename = filename;
        this.warning = warning;
    }
    
    //common functionality encapsulation
    //to enter a scope - to get a new environment
    private void enterScope(){
        Environment en = new Environment(current);
        current.addEnv(en);
        current = en;
    }
    //to exit a scope
    private void exitScope(){
        current = current.prev;
    }
    
    //to check whether already declared and to add to the symbol Table of the environment
    private boolean checkDeclandPut(String name,Descriptor des,int line,int col){
        Class c = des.getClass();  //to see what sort of a declaration is this
        
        //get the type and descriptor
        String type;
        if(c==MethodDescriptor.class){
            type = SemanticErrorPrint.METHOD;
        }
        else{
            type = SemanticErrorPrint.VARIABLE;
        }
        
        if(current.lookup(name)){
            //ERROR
           error.printDuplicateError(name, type, line, col);
           noOfErrors++;
           return false;
        }
        else{
            current.put(name, des);
            return true;
        }
    }
    //checks the type
    private boolean checkType(int actual, int tobechecked){
        return (actual != tobechecked && actual != ASType.ERROR && actual != ASType.NA);
    }
    //end of common functionality
    
    @Override
    public Object visit(ASProgram p) {
        
        top = new Environment(null);
        current = top;
        
        for(int i=0;i<p.fields.size();i++){
            ASFieldDecl f = p.fields.get(i); //casting unnecessary but check
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
            error.printMainNotDeclared(p.line,p.column);
            noOfErrors++;
        }
        else{
            //ERROR
            if(!mdes.parameters.isEmpty()){
                error.printMainError(mdes.line,mdes.column);
                noOfErrors++;
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
        checkDeclandPut(mdes.name, mdes, mdes.line, mdes.column);
        
        currentMethod = mdes;
        newMethod = true;    //to indicate a new method is here and for the block to not to create a new env.
        
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
        current = mdes.env;  //current is the environment inside method descriptor
        
        //traverse the block with in the method decl
        m.block.acceptWithReturn(this);  //block returns to the top

        currentMethod = null;
        
        return null;
        
    }

    @Override
    public Object visit(ASVariable var) {   //returning description to fill up the arraylist in method desc.
        VariableDescriptor des = new VariableDescriptor(var.type,var.name,var.line,var.column);
        
        if(checkDeclandPut(des.name, des,des.line , des.column)){
            return des;
        }
        else{
            return null;
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
        checkDeclandPut(des.name, des, des.line, des.column);        
        return null;
    }
    
    @Override
    public Object visit(ASBlock block) {
        
        if(newMethod){ //no need to create a new environment
            newMethod = false;
        }
        else{
            enterScope();
            
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
        
        exitScope();//end environment
        
        return null;
        
    }

    @Override
    public Object visit(ASAssignment assign) {
        
        ASType lhs = (ASType)assign.location.acceptWithReturn(this);
        ASType rhs = (ASType)assign.expr.acceptWithReturn(this);
        
        //must have same type
        if(assign.operator == ASAssignment.ASSIGN){
            //ERROR
            
            if((lhs.type != rhs.type) && 
               (rhs.type != ASType.NA) && 
               (lhs.type != ASType.ERROR && rhs.type != ASType.ERROR)){
                //error - means it is being dealt somewhere / NA - means a callout give a warning
                error.printTypeErrorVariable(lhs.stringType, rhs.stringType, assign.stringop,assign.line,assign.column);
                noOfErrors++;
            }
            
        }
        else{
            //ERROR
            if((lhs.type != ASType.INT || rhs.type != ASType.INT) && 
               (rhs.type != ASType.NA) && 
               (lhs.type != ASType.ERROR || rhs.type != ASType.ERROR)){
                
                error.printTypeErrorVariable(lhs.stringType, rhs.stringType, assign.stringop,assign.line,assign.column);
                noOfErrors++;
            }
            
        }
        
        return null;
    }

    @Override
    public Object visit(ASBreak b) {
        
        //ERROR
        if(!isFor){
            error.printBreakOrContError("break",b.line,b.column);
            noOfErrors++;
        }
        
        return null;
    }

    @Override
    public Object visit(ASContinue c) {
        
        //ERROR
        if(!isFor){
            error.printBreakOrContError("continue",c.line,c.column);
            noOfErrors++;
        }
        
        
        return null;
    }

    @Override
    public Object visit(ASFor f) {
        
        isFor = true;
        
        //entering a new scope
        enterScope();
        
        //put the new variable (shadows upper variables)
        VariableDescriptor des = new VariableDescriptor(new ASType("int"), f.var.name, f.line, f.column);
        checkDeclandPut(des.name, des, des.line, des.column);
        
        ASType start = (ASType)f.startExpr.acceptWithReturn(this);
        ASType end = (ASType)f.endExpr.acceptWithReturn(this);
        
        //ERROR
        if( (checkType(start.type,ASType.INT)) ||
            (checkType(end.type,ASType.INT))){
            error.printForError(start.stringType, end.stringType,f.line,f.column);
            noOfErrors++;
        }
        
        f.block.acceptWithReturn(this);
        
        //exiting the scope
        exitScope();
        isFor = false;
        
        return null;
        
    }

    @Override
    public Object visit(ASIf f) {
        
        ASType t = (ASType)f.condition.acceptWithReturn(this);
        
        //must be of boolean type
        //ERROR
        if(checkType(t.type,ASType.BOOLEAN)){
            error.printIfError(t.stringType,f.line,f.column);
            noOfErrors++;
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
                noOfErrors++;
            }
        }
        else{
            if(ret.returnExpr == null){
                error.printReturnTypeError(currentMethod.name, currentMethod.returnValue.stringType,ret.line,ret.column);
                noOfErrors++;
            }
            else{
                ASType t = (ASType)ret.returnExpr.acceptWithReturn(this);
                if(checkType(t.type,currentMethod.returnValue.type)){
                    error.printReturnTypeError(currentMethod.name, currentMethod.returnValue.stringType,ret.line,ret.column);
                    noOfErrors++;
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
            if(checkType(lhs.type,ASType.INT) || checkType(rhs.type,ASType.INT)){
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                noOfErrors++;
                return new ASType("error");
            }
            else if(ex.operator==ASBinaryExpr.GE ||
                    ex.operator==ASBinaryExpr.GT ||
                    ex.operator==ASBinaryExpr.LE ||
                    ex.operator==ASBinaryExpr.LT){
                return new ASType("boolean");
            }
            else{
                return new ASType("int");
            }
            
        }
        else if(ex.operator == ASBinaryExpr.EQ || ex.operator == ASBinaryExpr.NEQ){
            if((lhs.type != rhs.type) &&
               (lhs.type != ASType.ERROR && rhs.type != ASType.ERROR) &&
               (lhs.type != ASType.NA && rhs.type != ASType.NA)){
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                noOfErrors++;
                return new ASType("error");
            }
            else{
                return new ASType("boolean");
            }
        }
        else{
            if(checkType(lhs.type,ASType.BOOLEAN) || checkType(rhs.type, ASType.BOOLEAN)){
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                noOfErrors++;
                return new ASType("error");
            }
            else{
                return new ASType("boolean");
            }
        }
        
        
    }
    
    @Override
    public Object visit(ASUnaryExpr ex) {
        ASType t = (ASType)ex.expr.acceptWithReturn(this);
        if(ex.operator == ASUnaryExpr.MINUS){  //should be of type int
            //ERROR
            if(checkType(t.type,ASType.INT)){
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
            if(checkType(t.type,ASType.BOOLEAN)){
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
            noOfErrors++;
        }
        else{
            
            //ERROR
            if(des.getClass() != ArrayDescriptor.class){
                error.printArrayTypeError(array.name,array.line,array.column);
                noOfErrors++;
            }
            else{
                ArrayDescriptor ades = (ArrayDescriptor)des;
                ASType t = (ASType)array.location.acceptWithReturn(this);
                //ERROR
                if(checkType(t.type,ASType.INT)){
                    error.printArrayLocationError(array.name,array.line,array.column);
                    noOfErrors++;
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
        
        Descriptor des = (Descriptor)current.get(var.name);
        
        //ERROR
        if(des == null){
            error.printNotDeclared(var.name, SemanticErrorPrint.VARIABLE,var.line,var.column);
            noOfErrors++;
        }
        else{
            Class c = des.getClass();
            if(c == ArrayDescriptor.class){
                return ((ArrayDescriptor)des).type;
            }
            else{
                return ((VariableDescriptor)des).type;
            }
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
        
        //can be a normal or a library call
        ASType t = (ASType)call.method.acceptWithReturn(this);
        
        //this should return a value
        //ERROR
        if(t.type == ASType.VOID){
            error.printNoReturnVal(call.method.name, SemanticErrorPrint.METHOD,call.line,call.column);
            noOfErrors++;
            return new ASType("error");
        }
        else{
            if(warning && t.type == ASType.NA){
                error.printWarningAboutCallout(call.line, call.column);
                noOfErrors++;
            }
            return t;
        }

    }

    @Override
    public Object visit(ASStringLiteral l) {
        return null;
    }

   

    @Override
    public Object visit(ASLibraryCall m) {
        //no semantics - the type is not known; so not applicable
        return new ASType("na");
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
                    if(vdes.type.type != t.type && t.type != ASType.NA && t.type != ASType.ERROR){
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

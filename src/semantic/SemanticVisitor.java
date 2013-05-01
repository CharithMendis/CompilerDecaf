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
import ast.ASExpr;
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
import ir.high.IRLoad;
import ir.high.IRMethod;
import ir.high.IRStore;
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.Descriptor;
import semantic.symbol.Environment;
import semantic.symbol.FieldDescriptor;
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
    private VariableDescriptor currentParameter;
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
    
    private void putErrorType(AS as){
        as.typeIs = new ASType("error");
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
            m.parameters.get(i).acceptWithReturn(this);
            if(currentParameter!=null){
                currentParameter.kind = VariableDescriptor.PARA;
                mdes.parameters.add(currentParameter);
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
        
        if(current==top){
          des.kind = VariableDescriptor.FIELD;  
        }
 
        if(checkDeclandPut(des.name, des,des.line , des.column)){
            currentParameter = des;
        }
        else{
            currentParameter = null;
        }
        return null;

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
        array.typeIs = array.type;
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
        block.typeIs = new ASType("na");
        
        return null;
        
    }

    @Override
    public Object visit(ASAssignment assign) {
        
        assign.location.isStore = true;
        assign.location.acceptWithReturn(this);
        assign.expr.acceptWithReturn(this);
        
        ASType lhs = assign.location.typeIs;
        ASType rhs = assign.expr.typeIs;
        
        //must have same type
        if(assign.operator == ASAssignment.ASSIGN){
            //ERROR
            
            if((lhs.type != rhs.type) && 
               (rhs.type != ASType.NA) && 
               (lhs.type != ASType.ERROR && rhs.type != ASType.ERROR)){
                //error - means it is being dealt somewhere / NA - means a callout give a warning
                putErrorType(assign);
                error.printTypeErrorVariable(lhs.stringType, rhs.stringType, assign.stringop,assign.line,assign.column);
                noOfErrors++;
            }
            else{
                if(rhs.type == ASType.ERROR || lhs.type == ASType.ERROR) {
                    assign.typeIs = new ASType("error");
                }
            }
            
        }
        else{
            //ERROR
            if((lhs.type != ASType.INT || rhs.type != ASType.INT) && 
               (rhs.type != ASType.NA) && 
               (lhs.type != ASType.ERROR || rhs.type != ASType.ERROR)){
                putErrorType(assign);
                error.printTypeErrorVariable(lhs.stringType, rhs.stringType, assign.stringop,assign.line,assign.column);
                noOfErrors++;
            }
            else{
                if(rhs.type == ASType.ERROR || lhs.type == ASType.ERROR) {
                    assign.typeIs = new ASType("error");
                }
            }
            
        }
        
        return null;
    }

    @Override
    public Object visit(ASBreak b) {
        
        //ERROR
        if(!isFor){
            putErrorType(b);
            error.printBreakOrContError("break",b.line,b.column);
            noOfErrors++;
        }
        else{
            b.typeIs = new ASType("na");
        }
        
        return null;
    }

    @Override
    public Object visit(ASContinue c) {
        
        //ERROR
        if(!isFor){
            putErrorType(c);
            error.printBreakOrContError("continue",c.line,c.column);
            noOfErrors++;
        }
        else{
            c.typeIs = new ASType("na");
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
        des.kind = VariableDescriptor.LOCAL;
        checkDeclandPut(des.name, des, des.line, des.column);
        
        f.startExpr.acceptWithReturn(this);
        f.endExpr.acceptWithReturn(this);
        
        ASType start = f.startExpr.typeIs;
        ASType end = f.endExpr.typeIs;
        
        //ERROR
        if( (checkType(start.type,ASType.INT)) ||
            (checkType(end.type,ASType.INT))){
            putErrorType(f);
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
        
        f.condition.acceptWithReturn(this);
        ASType t = f.condition.typeIs;
        
        //must be of boolean type
        //ERROR
        if(checkType(t.type,ASType.BOOLEAN)){
            putErrorType(f);
            error.printIfError(t.stringType,f.line,f.column);
            noOfErrors++;
        }
        else{
            f.typeIs = f.condition.typeIs;
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
                putErrorType(ret);
                error.printNoReturnError(currentMethod.name,ret.line,ret.column);
                noOfErrors++;
            }
        }
        else{
            if(ret.returnExpr == null){
                putErrorType(ret);
                error.printReturnTypeError(currentMethod.name, currentMethod.returnValue.stringType,ret.line,ret.column);
                noOfErrors++;
            }
            else{
                ret.returnExpr.acceptWithReturn(this);
                ASType t = ret.returnExpr.typeIs;
                if(checkType(t.type,currentMethod.returnValue.type)){
                    putErrorType(ret);
                    error.printReturnTypeError(currentMethod.name, currentMethod.returnValue.stringType,ret.line,ret.column);
                    noOfErrors++;
                }
                else{
                    ret.typeIs = currentMethod.returnValue;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public Object visit(ASBinaryExpr ex) {
        
        ex.lhs.acceptWithReturn(this);
        ex.rhs.acceptWithReturn(this);
        
        ASType lhs = ex.lhs.typeIs;
        ASType rhs = ex.rhs.typeIs;
        
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
                putErrorType(ex);
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                noOfErrors++;
                
            }
            else if(ex.operator==ASBinaryExpr.GE ||
                    ex.operator==ASBinaryExpr.GT ||
                    ex.operator==ASBinaryExpr.LE ||
                    ex.operator==ASBinaryExpr.LT){
               ex.typeIs = new ASType("boolean");
            }
            else{
                ex.typeIs = new ASType("int");
            }
            
        }
        else if(ex.operator == ASBinaryExpr.EQ || ex.operator == ASBinaryExpr.NEQ){
            if((lhs.type != rhs.type) &&
               (lhs.type != ASType.ERROR && rhs.type != ASType.ERROR) &&
               (lhs.type != ASType.NA && rhs.type != ASType.NA)){
                putErrorType(ex);
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                noOfErrors++;
                
            }
            else{
                ex.typeIs = new ASType("boolean");
            }
        }
        else{
            if(checkType(lhs.type,ASType.BOOLEAN) || checkType(rhs.type, ASType.BOOLEAN)){
                putErrorType(ex);
                error.printTypeErrorVariable(lhs.stringType,rhs.stringType,ex.stringop,ex.line,ex.column);
                noOfErrors++;
            }
            else{
                ex.typeIs = new ASType("boolean");
            }
        }
        
        return null;
        
        
    }
    
    @Override
    public Object visit(ASUnaryExpr ex) {
        ex.expr.acceptWithReturn(this);
        ASType t = ex.expr.typeIs;
        if(ex.operator == ASUnaryExpr.MINUS){  //should be of type int
            //ERROR
            if(checkType(t.type,ASType.INT)){
                putErrorType(ex);
                error.printTypeErrorVariable("int", t.stringType,ex.line,ex.column);
                noOfErrors++;
            }
            else{
                ex.typeIs = t;
            }
        }
        else{  //should be of type boolean
            //ERROR
            if(checkType(t.type,ASType.BOOLEAN)){
                putErrorType(ex);
                error.printTypeErrorVariable("boolean", t.stringType,ex.line,ex.column);
                noOfErrors++;
            }
            else{
                ex.typeIs = t;
            }
        }
        
        return null;
    }

    @Override
    public Object visit(ASBooleanLiteral b) {
        b.typeIs = new ASType("boolean");  //characters are unsigned int (or part of integers)
        return null;
    }

    @Override
    public Object visit(ASCharLiteral c) {
        c.typeIs = new ASType("int");  //characters are unsigned int (or part of integers)
        return null;
    }

    @Override
    public Object visit(ASIntLiteral i) {
        i.typeIs = new ASType("int");
        return null;
    }

    //done
    @Override
    public Object visit(ASLocationArray array) {
        
        Descriptor des = current.get(array.name);

        //ERROR
        if(des == null){
            putErrorType(array);
            error.printNotDeclared(array.name, SemanticErrorPrint.VARIABLE,array.line,array.column);
            noOfErrors++;
        }
        else{   
            //ERROR
            if(des.getClass() != ArrayDescriptor.class){
                putErrorType(array);
                error.printArrayTypeError(array.name,array.line,array.column);
                noOfErrors++;
            }
            else{
                ArrayDescriptor ades = (ArrayDescriptor)des;
                array.location.acceptWithReturn(this);
                //ERROR
                if(checkType(array.location.typeIs.type,ASType.INT)){
                    putErrorType(array);
                    error.printArrayLocationError(array.name,array.line,array.column);
                    noOfErrors++;
                }
                else{
                    array.typeIs =  ades.type.element;
                }
            }
        }
            
        
        return null;
    }

    //done
    @Override
    public Object visit(ASLocationVar var) {
        
        Descriptor des = current.get(var.name);

        //ERROR
        if(des == null){ 
            putErrorType(var);
            error.printNotDeclared(var.name, SemanticErrorPrint.VARIABLE,var.line,var.column);
            noOfErrors++;
        }
        else{
            Class c = des.getClass();
            if(c == ArrayDescriptor.class || c == VariableDescriptor.class) {
                FieldDescriptor fdes = (FieldDescriptor)des;
                var.typeIs = fdes.type;
                if(var.isStore){
                    var.store = new IRStore(fdes);
                }
                else{
                    var.load = new IRLoad(fdes);
                } 
            }
            else{
                putErrorType(var);
                error.printNotVariableError(var.name, var.line, var.column);
                noOfErrors++;
            }
            
        }
        return null;
    }
    
    //done
    @Override
    public Object visit(ASMethodCallS call) {
        //can be a normal or a library call
        call.method.acceptWithReturn(this);
        
        //no need to check the return value here
        return null;
    }

    //done
    @Override
    public Object visit(ASMethodCallE call) {
        
        //can be a normal or a library call
        call.method.acceptWithReturn(this);
        
        //this should return a value
        //ERROR
        if(call.method.typeIs.type == ASType.VOID){
            putErrorType(call);
            error.printNoReturnVal(call.method.name, SemanticErrorPrint.METHOD,call.line,call.column);
            noOfErrors++;
        }
        else{
            call.typeIs = call.method.typeIs; //just a wrapper
            if(warning && call.method.typeIs.type == ASType.NA){
                error.printWarningAboutCallout(call.line, call.column);
            }
            
        }
        
        return null;

    }

    @Override
    public Object visit(ASStringLiteral l) {
        return null;
    }

   

    @Override
    public Object visit(ASLibraryCall m) {
        //no semantics - the type is not known; so not applicable
        m.typeIs = new ASType("na");
        return null;
    }

    @Override
    public Object visit(ASNormalCall asmethod) {
        //first check whether the method exists
        Descriptor des = current.get(asmethod.name);
        //ERROR
        if(des == null){  //no such method
            
            putErrorType(asmethod);
            error.printNotDeclared(asmethod.name,SemanticErrorPrint.METHOD,asmethod.line,asmethod.column);
            noOfErrors++;
        }
        else{
            
            if(des.getClass() != MethodDescriptor.class){   //well this is not a method
                putErrorType(asmethod);
                error.printNotMethodError(asmethod.name, asmethod.line, asmethod.column);
                noOfErrors++; 
                return null;
            }
            
            MethodDescriptor mdes = (MethodDescriptor)des;
            asmethod.method = new IRMethod(mdes);
            
            //now need to check for the expr types as well as the amount
            //check whether enough parameters are there
            //ERROR
            if(asmethod.arguments.size() != mdes.parameters.size()){
                error.printNotEnoughParameters(asmethod.name,asmethod.line,asmethod.column);
                noOfErrors++;
            }
            else{
                //type check
                for(int i=0;i<asmethod.arguments.size();i++){
                    ASExpr ex = asmethod.arguments.get(i);
                    ex.acceptWithReturn(this);
                    ASType t = ex.typeIs;
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
            asmethod.typeIs = mdes.returnValue;   
        }
        
        return null;
        
    }
 

    
    
}

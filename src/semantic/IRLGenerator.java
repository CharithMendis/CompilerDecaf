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
import ir.low.CJUMP;
import ir.low.CONST;
import ir.low.IRLActivation;
import ir.low.IRLArEx;
import ir.low.IRLBinaryEx;
import ir.low.IRLConEx;
import ir.low.IRLContainer;
import ir.low.IRLEx;
import ir.low.IRLLabel;
import ir.low.IRLMemLoc;
import ir.low.IRLRelEx;
import ir.low.IRLReturn;
import ir.low.IRLStm;
import ir.low.IRLTemp;
import ir.low.JUMP;
import ir.low.MOV;
import java.util.Stack;
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.Environment;
import semantic.symbol.FieldDescriptor;
import semantic.symbol.MethodDescriptor;
import semantic.symbol.VariableDescriptor;

/**
 *
 * @author Charith
 */
public class IRLGenerator implements VisitorWithReturn{

    Environment en;
    
    //global currents
    public IRLContainer currentContainer;
    public IRLActivation currentAct;
    public IRLStm currentStm;

    //break and continue jumps
    public Stack<JUMP> breakJump;
    public Stack<JUMP> contJump;
    
    
    public IRLGenerator(Environment en){
        this.en = en;
        breakJump = new Stack();
        contJump = new Stack();
    }
    
    void makeCurStm(IRLStm stm){
        currentStm.next = stm;
        currentStm = stm;
    }
    
    @Override
    public Object visit(ASProgram p) {
      currentContainer = new IRLContainer(en);
      for(int i=0;i<p.fields.size();i++){
          currentContainer.addField((FieldDescriptor)p.fields.get(i).acceptWithReturn(this));
      }
      for(int i=0;i<p.methods.size();i++){
          currentContainer.addActivation((IRLActivation)p.methods.get(i).acceptWithReturn(this));
      }
      return null;
      
    }

    @Override
    public Object visit(ASMethodDecl m) {
        MethodDescriptor mdes = (MethodDescriptor)en.get(m.name);
        
        IRLActivation now = new IRLActivation(mdes.name, mdes);
        currentAct = now;
        currentStm = now.head;
        
        m.block.acceptWithReturn(this);
        
        //leaving
        currentAct = null;
        currentStm = null;
        
        return now;
    }

    @Override
    public Object visit(ASVariable var) {
        VariableDescriptor f = (VariableDescriptor)en.get(var.name);
        return f;
    }

    @Override
    public Object visit(ASArray array) {
        ArrayDescriptor f = (ArrayDescriptor)en.get(array.name);
        return f;
    }

    @Override
    public Object visit(ASAssignment assign) {
        
        IRLEx lhs = (IRLEx)assign.location.acceptWithReturn(this); 
        IRLEx rhs = (IRLEx)assign.expr.acceptWithReturn(this);
        if(assign.operator != ASAssignment.ASSIGN){
            lhs = new IRLArEx(assign.stringop.substring(0,1),
                    rhs,
                    (IRLEx)assign.expr.acceptWithReturn(this));
        }
        
        IRLStm s = new MOV(rhs,lhs);
        
        currentStm.next = s;
        currentStm = s;
        
        return null;
    }

    @Override
    public Object visit(ASBreak b) {
        makeCurStm(breakJump.peek());
        return null;
    }

    @Override
    public Object visit(ASContinue c) {
        makeCurStm(contJump.peek());
        return null;
    }

    @Override
    public Object visit(ASFor f) {
        
        ASAssignment as = new ASAssignment("=",f.var, f.startExpr);
        as.acceptWithReturn(this);
        
        MOV mov = new MOV(new IRLTemp(),(IRLEx)f.endExpr.acceptWithReturn(this));
        
        makeCurStm(mov);
        
        //cjump
        IRLLabel falseLabel = new IRLLabel();
        CJUMP cjump = new CJUMP(new IRLConEx("<=",(IRLEx)f.var.acceptWithReturn(this), mov.to), 
                null, falseLabel);
        cjump.own = new IRLLabel();
        
        breakJump.push(new JUMP(falseLabel));
        
        //incrementing
        MOV inc = new MOV((IRLEx)f.var.acceptWithReturn(this),
                new IRLArEx("+",(IRLEx)f.var.acceptWithReturn(this), new CONST(1)));
        inc.own = new IRLLabel();
        
        contJump.push(new JUMP(inc.own));
        
        makeCurStm(cjump);
        currentStm = cjump.nextT;
        f.block.acceptWithReturn(this);
        
        makeCurStm(inc);
        
        //jump back to the cjump statement
        JUMP jump = new JUMP(cjump.own);
        
        makeCurStm(jump);
        
        //cjump.nextF.next = null;
        currentStm = cjump;
        
        //pop the values of break and cont jumps
        breakJump.pop();
        contJump.pop();
        
        
        return null;
        
        
    }

    @Override
    public Object visit(ASIf f) {
        
        IRLEx ifex = (IRLEx)f.condition.acceptWithReturn(this);
        CJUMP cjump = new CJUMP(ifex, new IRLLabel(), new IRLLabel());
        currentStm.next = cjump;
        
        currentStm = cjump.nextT;
        f.ifstat.acceptWithReturn(this);
        
        if(f.elsePresent){
            currentStm = cjump.nextF;
            f.elsestat.acceptWithReturn(this);
        }
        
        currentStm = cjump;
        return null;
        
    }

    @Override
    public Object visit(ASMethodCallS call) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visit(ASReturn ret) {
        IRLReturn r;
        if(ret.returnExpr!= null){
            r = new IRLReturn(new MOV(new IRLTemp(),(IRLEx)ret.returnExpr.acceptWithReturn(this)));
        }
        else{
            r = new IRLReturn(null);
        }
        currentStm.next = r;
        currentStm = r;
        return null;
    }

    @Override
    public Object visit(ASBinaryExpr ex) {
        if(ASBinaryExpr.isArithmetic(ex.operator)){
            IRLEx lhs = (IRLEx)ex.lhs.acceptWithReturn(this);
            IRLEx rhs = (IRLEx)ex.rhs.acceptWithReturn(this);
            return new IRLArEx(ex.stringop,lhs,rhs);
        }
        else if(ASBinaryExpr.isConditional(ex.operator)){
            IRLEx lhs = (IRLEx)ex.lhs.acceptWithReturn(this);
            IRLEx rhs = (IRLEx)ex.rhs.acceptWithReturn(this);
            return new IRLConEx(ex.stringop,lhs,rhs);
        }
        else{
            IRLEx lhs = (IRLEx)ex.lhs.acceptWithReturn(this);
            IRLEx rhs = (IRLEx)ex.rhs.acceptWithReturn(this);
            IRLLabel t = new IRLLabel();
            IRLLabel f = new IRLLabel();
            if(ex.operator == ASBinaryExpr.AND){
                lhs.falseEx = f;
                lhs.trueEx = rhs;
                rhs.falseEx = f;
                rhs.trueEx = t;
            }
            else{
                lhs.falseEx = rhs;
                lhs.trueEx = t;
                rhs.falseEx = f;
                rhs.trueEx = t;
            }  
            return new IRLRelEx(ex.stringop, lhs, null);
        }
    }

    @Override
    public Object visit(ASBooleanLiteral b) {
        return new CONST(b.bool);
    }

    @Override
    public Object visit(ASCharLiteral c) {
        return new CONST(c.character);
    }

    @Override
    public Object visit(ASIntLiteral i) {
        return new CONST(i.integer);
    }

    @Override
    public Object visit(ASLocationArray array) {
        IRLEx index = (IRLEx)array.location.acceptWithReturn(this);
        if(array.isStore){
            return new IRLMemLoc(index,array.store.fdes);
        }
        else{
            return new IRLMemLoc(index,array.load.fdes);
        }
    }

    @Override
    public Object visit(ASLocationVar var) {
        if(var.isStore){
            return new IRLMemLoc(var.store.fdes);
        }
        else{
            return new IRLMemLoc(var.load.fdes);
        }
    }

    @Override
    public Object visit(ASMethodCallE call) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visit(ASStringLiteral l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visit(ASUnaryExpr ex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visit(ASLibraryCall m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visit(ASNormalCall l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object visit(ASBlock block) {
       
        //IRLStm now = currentStm;
        for(int i=0;i<block.statements.size();i++){
            block.statements.get(i).acceptWithReturn(this);   
        }
        //currentStm = now;
        return null;
    }
    
}

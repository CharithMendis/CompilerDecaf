/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;


import ast.ASBinaryExpr;
import ast.ASType;
import ir.low.CALL;
import ir.low.CJUMP;
import ir.low.CONST;
import ir.low.IRLActivation;
import ir.low.IRLArEx;
import ir.low.IRLCallE;
import ir.low.IRLCallS;
import ir.low.IRLConEx;
import ir.low.IRLContainer;
import ir.low.IRLEx;
import ir.low.IRLLabel;
import ir.low.IRLMemLoc;
import ir.low.IRLRelEx;
import ir.low.IRLReturn;
import ir.low.IRLStm;
import ir.low.IRLTemp;
import ir.low.IRLTerStm;
import ir.low.JUMP;
import ir.low.MOV;
import ir.low.NEG;
import ir.low.STRING;
import java.io.BufferedWriter;
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.FieldDescriptor;


/**
 *
 * @author Charith
 */
public class CGActivation_3 implements VisitorIR{
    
    String currentString;
    BufferedWriter buf;
    CGTranslate trans;
    
    public final String EAX = "%eax";
    public final String EBX = "%ebx";
    public final String ECX = "%ecx";
    public final String EDX = "%edx";
    public final String ESP = "%esp";
    public final String EBP = "%ebp";
    

    public CGActivation_3(BufferedWriter buf) {
        this.buf = buf;
        trans = new CGTranslate();
        
    }
    
    
    
    //statements should visit next statement
    Object visitNext(IRLStm stm,Object o) throws Exception{
        if(stm.next != null){
            return stm.next.accept(this, o);
        }
        else{
            return null;
        }
    }
    
    Object visitEx(IRLEx ex) throws Exception{
        if(ex.trueEx != null){
            ex.trueEx.accept(this, ex);
        }
        if(ex.falseEx != null){
            ex.falseEx.accept(this, ex);
        }
        return null;  
    }
    
    void append(String s){
        currentString += s;
    }
    /////////////////////////////////////////

    @Override
    public Object visit(IRLContainer ir, Object o) throws Exception {
        for(int i=0;i<ir.getFieldCount();i++){
            FieldDescriptor des = ir.getField(i);
        }
        
        //house cleaning
        buf.write(".section .text\n\n");
        buf.write(".globl _main\n\n");
        //end of house cleaning
        
        for(int i=0;i<ir.getActivationCount();i++){
            ir.getActivation(i).accept(this, o);
        }
        return null;
    }

    @Override
    public Object visit(IRLActivation act, Object o) throws Exception {
        
        currentString = act.code;
        act.name.accept(this, o);
        
        append("\tenter $" + String.valueOf(act.localSize*4) + ", $0\n");
        
        //save the callee save registers - change code in the temp allocation to imclude these register counts
         
        buf.write(currentString);

        act.head.accept(this, o);
        
        //restore the callee saved registers
        if(act.mdes.returnValue.type == ASType.VOID){
            buf.write("\tleave\n\tret\n\n");   //to be safe that a return is there
        }
        
        return null;
    }

   

    @Override
    public Object visit(CJUMP cjump, Object o) throws Exception {
        
        if(cjump.own != null){       //sometimes may have own label - in future we can have all cjumps have there own label
            cjump.own.accept(this, o);
        }
        
        cjump.ex.accept(this, o);
        
        
        cjump.nextT.accept(this, o);   //this is a trm stm - may have other statements
        
        if(cjump.f != null){          //false or true labels may be there
            cjump.f.accept(this, o);
        }
        
        cjump.nextF.accept(this, o);  //this is a trm stm - may have other statements
        visitNext(cjump, o);
        
        return null;
    }

    @Override
    public Object visit(IRLReturn ret, Object o) throws Exception {
        
        currentString = ret.code;
        
        if(ret.ex!=null){
           String s = (String)ret.ex.accept(this, o);
           append(trans.movCode(s, EAX));
        }
        append(trans.returnCode());
        
        buf.write(currentString);
        
        visitNext(ret, o);
        
        return null;
        
    }

    @Override
    public Object visit(IRLTerStm stm, Object o) throws Exception {
        visitNext(stm, o);
        return null;
    }

    @Override
    public Object visit(JUMP jump, Object o) throws Exception {
        jump.where.accept(this, o);
        visitNext(jump, o);
        return null;
    }

    @Override
    public Object visit(MOV mov, Object o) throws Exception {
        
        currentString = mov.code;
        
        if(mov.own != null){
            mov.own.accept(this, o);
        }
        String s1 = (String)mov.from.accept(this, o);
        String s2 = (String)mov.to.accept(this, o);
        
        append(trans.movCode(s1, EBX));
        append(trans.movCode(EBX, s2));
        
        buf.write(currentString);
        
        visitNext(mov, o);
        
        return null;
    }
    
    @Override
    public Object visit(IRLCallS calls, Object o) throws Exception {
        
        currentString = calls.code;
        
        calls.call.accept(this, o);
        
        append(trans.addSubMulCode("+","$" + String.valueOf(calls.call.arguments.size()*4), ESP));   //remove parameters
        
        //pop the caller saved registers
        
        buf.write(currentString);
        
        visitNext(calls,0);
        return null;
    }

    @Override
    public Object visit(CALL call, Object o) throws Exception {
        
        //first store the caller saved registers
        
        for(int i=call.arguments.size()-1;i>=0;i--){  //arguments go the other way around
           String s  = (String)call.getArgument(i).accept(this, o);
           append(trans.pushCode(s)); //push the parameters
        }
        
        append(trans.callCode(call.name.name));  //call the function
        return null;
       
    }

    @Override
    public Object visit(IRLCallE calle, Object o) throws Exception {
        
        calle.call.accept(this, o);
        
        append(trans.addSubMulCode("+","$" + String.valueOf(calle.call.arguments.size()*4), ESP));   //remove parameters
        append(trans.pushCode(EAX));
        
        //pop the caller saved registers
        visitEx(calle);
        
        return calle.location.getRegister();
    }

    @Override
    public Object visit(CONST c, Object o) throws Exception {
        visitEx(c);  //need to expand this as well
        return "$" + String.valueOf(c.val);
    }

    //done
    @Override
    public Object visit(IRLArEx ar, Object o) throws Exception {
        
        String s1 = (String)ar.lhs.accept(this, o);
        String s2 = (String)ar.rhs.accept(this, o);
        //cannot have true or false statements
        if(ar.stringop.equals("+") || ar.stringop.equals("-") || ar.stringop.equals("*")){
            append(trans.movCode(s1, EBX));
            append(trans.addSubMulCode(ar.stringop, s2, EBX));
            append(trans.pushCode(EBX));
        }
        else if(ar.stringop.equals("/")){
            append(trans.movCode(s1, EAX));
            append("\tcdq\n");                   //extend to quad word
            append(trans.movCode(s2, EBX));
            append(trans.divModCode(ar.stringop, EBX));
            append(trans.pushCode(EAX));
        }
        else{
            append(trans.movCode(s1, EAX));
            append("\tcdq\n");
            append(trans.movCode(s2, EBX));
            append(trans.divModCode(ar.stringop, EBX));
            append(trans.pushCode(EDX));
        }
        
        return ar.location.getRegister();
    }

    @Override
    public Object visit(IRLConEx con, Object o) throws Exception {
        con.lhs.accept(this, o);
        con.rhs.accept(this, o);
        visitEx(con);
        return null;
    }

    @Override
    public Object visit(IRLRelEx rel, Object o) throws Exception {
        rel.lhs.accept(this, o);
        visitEx(rel);
        return null;
    }

    @Override
    public Object visit(IRLLabel label, Object o) throws Exception {
        
        append(label.name + ":\n");
        
        return null;
    }

    @Override
    public Object visit(IRLTemp temp, Object o) throws Exception {
        return null;
    }

    @Override
    public Object visit(NEG neg, Object o) throws Exception {
        neg.ex.accept(this, o);
        visitEx(neg);
        return null;
    }

    @Override
    public Object visit(STRING string, Object o) throws Exception {
        return "$" + string.label.name;
        
    }

    @Override
    public Object visit(IRLMemLoc loc, Object o) throws Exception {
        //arrays have the expr 
        String ret = "";
        if(loc.fdes.getClass() == ArrayDescriptor.class){
           String where = (String)loc.expr.accept(this, o);
           append(trans.movCode(where,ECX));    //arrays are stored in ecx
           ret = loc.fdes.name + "( , " + ECX + ", 4)"; 
        }
        else{
            if(loc.fdes.loc == null){   //global variable
                ret = "(" + loc.fdes.name + ")";
            }
            else{
                ret = loc.fdes.loc.getRegister();
            }
        }
        visitEx(loc);   //need to expand this as well
        return ret;
    }
    
    
}

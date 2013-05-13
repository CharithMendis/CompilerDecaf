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
import java.util.ArrayList;
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.FieldDescriptor;


/**
 *
 * @author Charith
 */
public class CGActivation2_3 implements VisitorIR{
    
    //String currentString;
    BufferedWriter buf;
    CGTranslate trans;
    
    public final String EAX = "%eax";
    public final String EBX = "%ebx";
    public final String ECX = "%ecx";
    public final String EDX = "%edx";
    public final String ESP = "%esp";
    public final String EBP = "%ebp";


    public CGActivation2_3(BufferedWriter buf) {
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
    
    Object visitEx(IRLEx ex,String op,String arg1,String arg2,Object o) throws Exception{
        
        //comaprison with eax - load to eax arg2 and then compare then jump to label
        //determine which is a label the trueEx or falseEx
        int label = 2;
        if(ex.trueEx != null && ex.trueEx.getClass() == IRLLabel.class){
            label = 1;
        }
        if(ex.falseEx != null && ex.falseEx.getClass() == IRLLabel.class){
            label = 0;
        }
        
        if(label!=2){
            append(trans.movCode(arg1, EAX));
            append(trans.cmpCode(arg2, EAX));
        }
        
        if(label == 1){
            append(trans.jumpCode(op, ((IRLLabel)ex.trueEx).name, false));
            if(ex.falseEx!=null && ex.falseEx.getClass() != IRLLabel.class){  //if it is a label it should be the first statement
                ex.falseEx.accept(this, o);
            }
        }
        else if(label == 0){
            append(trans.jumpCode(op, ((IRLLabel)ex.falseEx).name, true));
            if(ex.trueEx != null && ex.trueEx.getClass() != IRLLabel.class){   //if it is a label it should be the first statement
                ex.trueEx.accept(this, o);
            }
        }
        
        return null;  
    }
    
    void append(String s)throws Exception{
        buf.write(s);
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
        
        act.name.accept(this, o);
        
        append("\tenter $" + String.valueOf(act.localSize*4) + ", $0\n");
        
        //save the callee save registers - change code in the temp allocation to imclude these register counts
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
        

        cjump.t.accept(this, o);

        
        cjump.nextT.accept(this, cjump.noTemp);   //pass the amount of temps to jump
        
        
        append(trans.jumpCode("jmp", cjump.jumpAfterFalse.name, true));
        

        cjump.f.accept(this, o);

        
        cjump.nextF.accept(this, o);  //this is a trm stm - may have other statements
        
        cjump.jumpAfterFalse.accept(this, o);
        
        visitNext(cjump, o);
        
        return null;
    }

    @Override
    public Object visit(IRLReturn ret, Object o) throws Exception {
        
        
        if(ret.ex!=null){
           String s = (String)ret.ex.accept(this, o);
           append(trans.movCode(s, EAX));
        }
        append(trans.returnCode());
        
        
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
        
        if(jump.havePara){   //temp fix for loops
            append(trans.addSubMulCode("+", "$" + String.valueOf((int)o*4), ESP));
            
        }
        
        if(jump.own != null){
            jump.own.accept(this, o);
        }
        
        //jump.where.accept(this, o);
        append(trans.jumpCode("jmp", jump.where.name, false));
        
        
        visitNext(jump, o);
        return null;
    }

    @Override
    public Object visit(MOV mov, Object o) throws Exception {
        
        
        if(mov.own != null){
            mov.own.accept(this, o);
        }
        String s1 = (String)mov.from.accept(this, o);
        String s2 = (String)mov.to.accept(this, o);
        
        append(trans.movCode(s1, EBX));
        append(trans.movCode(EBX, s2));
        
        visitNext(mov, o);
        
        return null;
    }
    
    @Override
    public Object visit(IRLCallS calls, Object o) throws Exception {
        
        
        calls.call.accept(this, o);
        
        append(trans.addSubMulCode("+","$" + String.valueOf(calls.call.arguments.size()*4), ESP));   //remove parameters
        
        //pop the caller saved registers
        
        
        visitNext(calls,o);
        return null;
    }

    @Override
    public Object visit(CALL call, Object o) throws Exception {
        
        //first store the caller saved registers
        ArrayList<String> arg = new ArrayList();
        
        int keep = 0;
        
        for(int i=call.arguments.size()-1;i>=0;i--){  //arguments go the other way around
           String s  = (String)call.getArgument(i).accept(this, o);
           
           if(s.equals("(%esp)")){
               int t = call.getArgument(i).location.espOffset + keep;
               s = String.valueOf(t*4) + "(%esp)";
           }
           arg.add(s);
           keep++;
        }
        
        for(int i=0;i<arg.size();i++){
            append(trans.pushCode(arg.get(i))); //push the parameters
        }
        
        append(trans.callCode(call.name.name));  //call the function
        append(trans.addSubMulCode("+", "$" + String.valueOf(call.totalTemp*4), ESP));
        //System.out.println(call.name.name + String.valueOf(call.totalTemp));
        return null;
       
    }

    @Override
    public Object visit(IRLCallE calle, Object o) throws Exception {
        
        calle.call.accept(this, o);
        
        append(trans.addSubMulCode("+","$" + String.valueOf(calle.call.arguments.size()*4), ESP));   //remove parameters
        append(trans.pushCode(EAX));
        
        //pop the caller saved registers
        visitEx(calle, "==", calle.location.getRegister(), "$1",0);
        
        return calle.location.getRegister();
    }

    @Override
    public Object visit(CONST c, Object o) throws Exception {
        visitEx(c, "==","$" + String.valueOf(c.val),"$1",o);
        return "$" + String.valueOf(c.val);
    }

    //done
    @Override
    public Object visit(IRLArEx ar, Object o) throws Exception {
        
        //cannot have true or false statements
        String s1 = (String)ar.lhs.accept(this, o);
        String s2 = (String)ar.rhs.accept(this, o);
        
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
        
        String lhs = (String)con.lhs.accept(this, o);
        String rhs = (String)con.rhs.accept(this, o);
        
        visitEx(con, con.stringop, lhs, rhs,o);
        
        String ret = null;
        if(con.isStored){
            //true
            con.trueLabel.accept(this, o);
            //store the value and jump
            append(trans.movCode("$1", con.location.getRegister()));
            append(trans.jumpCode("jmp", con.jumpAfterFalse.name, true));
            //false
            con.falseLabel.accept(this, o);
            //store the value
            append(trans.movCode("$0", con.location.getRegister()));
            //resume
            con.jumpAfterFalse.accept(this, o);
            ret = con.location.getRegister();
        }
        
        return ret;
    }

    @Override
    public Object visit(IRLRelEx rel, Object o) throws Exception {
        
        //first determine if the value need to be stored

        rel.lhs.accept(this, o);
        
        //change the code to a seperate function
 
        String ret = null;
        if(rel.isStored){
            
            if(rel.stringop.equals("&&")){
                rel.trueLabel.accept(this, o);
                append(trans.movCode("$1", rel.location.getRegister()));
                append(trans.jumpCode("jmp", rel.jumpAfterFalse.name, true));
                //false
                rel.falseLabel.accept(this, o);
                //store the value
                append(trans.movCode("$0", rel.location.getRegister()));
                //resume
                rel.jumpAfterFalse.accept(this, o);
                ret = rel.location.getRegister();
            }
            else{
                rel.falseLabel.accept(this, o);
                append(trans.movCode("$0", rel.location.getRegister()));
                append(trans.jumpCode("jmp", rel.jumpAfterFalse.name, true));
                
                rel.trueLabel.accept(this, o);
                append(trans.movCode("$1", rel.location.getRegister()));

                //resume
                rel.jumpAfterFalse.accept(this, o);
                ret = rel.location.getRegister();
            }
            
        }
        else{
            if(rel.stringop.equals("&&")){
                rel.trueLabel.accept(this, o);
                if(rel.trueEx.getClass() != IRLLabel.class){
                    rel.trueEx.accept(this, o);
                }
                else{
                    append(trans.jumpCode("jmp", ((IRLLabel)rel.trueEx).name, true));
                }
                rel.falseLabel.accept(this, o);
                if(rel.falseEx.getClass() != IRLLabel.class){
                    rel.falseEx.accept(this, o);
                }
                else{
                    append(trans.jumpCode("jmp", ((IRLLabel)rel.falseEx).name, false));
                }
            }
            else{
                
                rel.falseLabel.accept(this, o);
                if(rel.falseEx.getClass() != IRLLabel.class){
                    rel.falseEx.accept(this, o);
                }
                else{
                    append(trans.jumpCode("jmp", ((IRLLabel)rel.falseEx).name, false));
                }
                rel.trueLabel.accept(this, o);
                if(rel.trueEx.getClass() != IRLLabel.class){
                    rel.trueEx.accept(this, o);
                }
                else{
                    append(trans.jumpCode("jmp", ((IRLLabel)rel.trueEx).name, true));
                }
                
            }
        }
        
        return ret;
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
        
        String place = (String)neg.ex.accept(this, o);
        
        
        append(trans.movCode(place, EAX));
        append(trans.xorCode("$1", EAX));
        append(trans.movCode(EAX, neg.location.getRegister()));
        
        visitEx(neg, "==", neg.location.getRegister(), "$1",o);
        return neg.location.getRegister();
    }

    @Override
    public Object visit(STRING string, Object o) throws Exception {
        return "$" + string.label.name;
        
    }

    @Override
    public Object visit(IRLMemLoc loc, Object o) throws Exception {
        //arrays have the expr 
        String ret;
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
                ret = loc.location.getRegister();
            }
        }
        visitEx(loc, "==", ret, "$1",o);   //need to expand this as well
        return ret;
    }
    
    
}

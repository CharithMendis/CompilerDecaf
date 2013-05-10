/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

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
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.FieldDescriptor;

/**
 *
 * @author Charith
 */

//this will be constantly updated when various traversals are written

public class IRLPrinter implements VisitorIR{

    //common functions
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
    
    void tempPrinter(IRLEx ex) throws Exception{
        if(ex.location != null){
            ex.location.accept(this, null);
        }
    }
    //end of common functions
    
    @Override
    public Object visit(IRLContainer ir, Object o) throws Exception {
        for(int i=0;i<ir.getFieldCount();i++){
            FieldDescriptor des = ir.getField(i);
            des.print();
        }
        for(int i=0;i<ir.getActivationCount();i++){
            ir.getActivation(i).accept(this, o);
        }
        return null;
    }

    @Override
    public Object visit(IRLActivation act, Object o) throws Exception {
        System.out.println("Activation: ");
        act.name.accept(this, o);
        System.out.println("size: " + String.valueOf(act.localSize));
        act.head.accept(this, o);
        
        return null;
    }

    @Override
    public Object visit(IRLCallS calls, Object o) throws Exception {
        System.out.println("CALLS: ");
        calls.call.accept(this, o);
        visitNext(calls,0);
        return null;
    }

    @Override
    public Object visit(CJUMP cjump, Object o) throws Exception {
        
        System.out.println("CJUMP: ");
        if(cjump.own != null){       //sometimes may have own label - in future we can have all cjumps have there own label
            System.out.print("own ");
            cjump.own.accept(this, o);
        }
        
        cjump.ex.accept(this, o);
        
        
        cjump.nextT.accept(this, o);   //this is a trm stm - may have other statements
        
        if(cjump.f != null){          //false or true labels may be there
            System.out.print("false ");
            cjump.f.accept(this, o);
        }
        
        cjump.nextF.accept(this, o);  //this is a trm stm - may have other statements
        visitNext(cjump, o);
        
        return null;
    }

    @Override
    public Object visit(IRLReturn ret, Object o) throws Exception {
        System.out.println("Return: ");
        if(ret.ex!=null){
            ret.ex.accept(this, o);
        }
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
        System.out.println("JUMP: ");
        jump.where.accept(this, o);
        visitNext(jump, o);
        return null;
    }

    @Override
    public Object visit(MOV mov, Object o) throws Exception {
        System.out.println("MOV: ");
        if(mov.own != null){
            mov.own.accept(this, o);
        }
        mov.from.accept(this, o);
        mov.to.accept(this, o);
        visitNext(mov, o);
        
        return null;
    }
    
    //expressions

    @Override
    public Object visit(CALL call, Object o) throws Exception {
        tempPrinter(call);
        call.name.accept(this, o);
        for(int i=0;i<call.arguments.size();i++){
            call.getArgument(i).accept(this, o);
        }
        return null;
       
    }

    @Override
    public Object visit(IRLCallE calle, Object o) throws Exception {
        tempPrinter(calle);
        System.out.println("CALLE: ");
        calle.call.accept(this, o);
        visitEx(calle);
        return null;
    }

    @Override
    public Object visit(CONST c, Object o) throws Exception {
        tempPrinter(c);
        System.out.println("CONST: " + c.val);
        visitEx(c);
        return null;
    }

    @Override
    public Object visit(IRLArEx ar, Object o) throws Exception {
        tempPrinter(ar);
        System.out.println("Arithmetic: ");
        ar.lhs.accept(this, o);
        ar.rhs.accept(this, o);
        //cannot have true or false statements
        return null;
    }

    @Override
    public Object visit(IRLConEx con, Object o) throws Exception {
        tempPrinter(con);
        System.out.println("Conditional: ");
        con.lhs.accept(this, o);
        con.rhs.accept(this, o);
        visitEx(con);
        return null;
    }

    @Override
    public Object visit(IRLRelEx rel, Object o) throws Exception {
        
        tempPrinter(rel);
        System.out.println("Relational: ");
        rel.lhs.accept(this, o);
        visitEx(rel);
        return null;
    }

    @Override
    public Object visit(IRLLabel label, Object o) throws Exception {
        tempPrinter(label);
        System.out.println("label: " + label.name);
        return null;
    }

    @Override
    public Object visit(IRLTemp temp, Object o) throws Exception {
        System.out.println("TEMP: " + temp.getRegister());
        return null;
    }

    @Override
    public Object visit(NEG neg, Object o) throws Exception {
        tempPrinter(neg);
        System.out.println("NEG: ");
        neg.ex.accept(this, o);
        visitEx(neg);
        return null;
    }

    @Override
    public Object visit(STRING string, Object o) throws Exception {
        System.out.println("STRING: " + string.value);
        string.label.accept(this, o);
        return null;
    }

    @Override
    public Object visit(IRLMemLoc loc, Object o) throws Exception {
        
        //arrays have the expr
        tempPrinter(loc);
        System.out.println("MEMLOC: " + loc.fdes.name);
        if(loc.fdes.getClass() == ArrayDescriptor.class){
            System.out.println("global");
            loc.expr.accept(this, o);
        }
        else{
            if(loc.fdes.loc == null){ 
                System.out.println("global");
            }
        }
        
        
        return null;
    }
    
    
    
}

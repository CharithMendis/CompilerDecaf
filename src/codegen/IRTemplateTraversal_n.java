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
public class IRTemplateTraversal_n implements VisitorIR{
    
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

    @Override
    public Object visit(IRLContainer ir, Object o) throws Exception {
        for(int i=0;i<ir.getFieldCount();i++){
            FieldDescriptor des = ir.getField(i);
        }
        for(int i=0;i<ir.getActivationCount();i++){
            ir.getActivation(i).accept(this, o);
        }
        return null;
    }

    @Override
    public Object visit(IRLActivation act, Object o) throws Exception {
        act.name.accept(this, o);
        act.head.accept(this, o);
        
        return null;
    }

    @Override
    public Object visit(IRLCallS calls, Object o) throws Exception {
        calls.call.accept(this, o);
        visitNext(calls,0);
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
        if(ret.mov!=null){
            ret.mov.accept(this, o);
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
        jump.where.accept(this, o);
        visitNext(jump, o);
        return null;
    }

    @Override
    public Object visit(MOV mov, Object o) throws Exception {
        
        mov.from.accept(this, o);
        mov.to.accept(this, o);
        visitNext(mov, o);
        
        return null;
    }

    @Override
    public Object visit(CALL call, Object o) throws Exception {
        call.name.accept(this, o);
        for(int i=0;i<call.arguments.size();i++){
            call.getArgument(i).accept(this, o);
        }
        return null;
       
    }

    @Override
    public Object visit(IRLCallE calle, Object o) throws Exception {
        calle.call.accept(this, o);
        visitEx(calle);
        return null;
    }

    @Override
    public Object visit(CONST c, Object o) throws Exception {
        visitEx(c);
        return null;
    }

    @Override
    public Object visit(IRLArEx ar, Object o) throws Exception {
        ar.lhs.accept(this, o);
        ar.rhs.accept(this, o);
        //cannot have true or false statements
        return null;
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
        string.label.accept(this, o);
        return null;
    }

    @Override
    public Object visit(IRLMemLoc loc, Object o) throws Exception {
        //arrays have the expr        
        if(loc.fdes.getClass() == ArrayDescriptor.class){
            loc.expr.accept(this, o);
        }
        else{
            if(loc.fdes.loc != null) {
                loc.fdes.loc.accept(this, o);
            }
        }
        visitEx(loc);
        return null;
    }
    
    
    
}

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
import ir.low.IRLLabel;
import ir.low.IRLMemLoc;
import ir.low.IRLRelEx;
import ir.low.IRLReturn;
import ir.low.IRLTemp;
import ir.low.IRLTerStm;
import ir.low.JUMP;
import ir.low.MOV;
import ir.low.NEG;
import ir.low.STRING;

/**
 *
 * @author Charith
 */
public interface VisitorIR {
    
    public Object visit(IRLContainer ir,Object o) throws Exception;
    
    //activation
    public Object visit(IRLActivation act,Object o) throws Exception;
    
    //stm
    public Object visit(IRLCallS call,Object o) throws Exception;
    public Object visit(CJUMP cjump,Object o) throws Exception;
    public Object visit(IRLReturn ret,Object o) throws Exception;
    public Object visit(IRLTerStm stm,Object o) throws Exception;
    public Object visit(JUMP jump,Object o) throws Exception;
    public Object visit(MOV mov,Object o) throws Exception;
    
    //ex
    public Object visit(CALL call,Object o) throws Exception;
    public Object visit(IRLCallE calle,Object o) throws Exception;
    public Object visit(CONST c,Object o) throws Exception;
    public Object visit(IRLArEx ar,Object o) throws Exception;
    public Object visit(IRLConEx con,Object o) throws Exception;
    public Object visit(IRLRelEx rel,Object o) throws Exception;
    public Object visit(IRLLabel label,Object o) throws Exception;
    public Object visit(IRLTemp temp,Object o) throws Exception;
    public Object visit(NEG neg,Object o) throws Exception;
    public Object visit(STRING string,Object o) throws Exception;
    public Object visit(IRLMemLoc string,Object o) throws Exception;
    
}

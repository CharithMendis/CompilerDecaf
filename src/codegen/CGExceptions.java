/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ast.ASType;
import ir.low.CALL;
import ir.low.CJUMP;
import ir.low.CONST;
import ir.low.IRLActivation;
import ir.low.IRLCallS;
import ir.low.IRLConEx;
import ir.low.IRLEx;
import ir.low.IRLLabel;
import ir.low.IRLMemLoc;
import ir.low.IRLReturn;
import ir.low.JUMP;
import ir.low.STRING;
import semantic.symbol.Environment;
import semantic.symbol.FieldDescriptor;
import semantic.symbol.MethodDescriptor;
import semantic.symbol.VariableDescriptor;

/**
 *
 * @author Charith
 */
public class CGExceptions {
    
    
    final static String JUMP_OBE = ".OBE";
    final static String JUMP_NRE = ".NRE";
    
    static int number = 0;
  
    IRLActivation getGenericException(String name,String message,Environment en){
        
        MethodDescriptor mdes = new MethodDescriptor(new ASType("void"), name, new Environment(en), 0, 0);
        //last three arguments current env, line, col
        VariableDescriptor vdes = new VariableDescriptor(new ASType("int"), "temp", 0, 0);
        vdes.kind = VariableDescriptor.PARA;
        mdes.parameters.add(vdes);
        mdes.env.put("temp", vdes);
        en.put(name, mdes);
      
        
        IRLActivation act = new IRLActivation(mdes.name, mdes);
        
        CALL call = new CALL(new IRLLabel("_printf"));
        call.addArgument(new STRING(null,message));
        call.addArgument(new IRLMemLoc((FieldDescriptor)mdes.env.get("temp")));
        IRLCallS  calls = new IRLCallS(call);
        
        //exit in windows
        //push $1
	//call _ExitProcess@4 
        CALL callexit = new CALL(new IRLLabel("_ExitProcess@4 "));
        callexit.addArgument(new CONST(1));
        IRLCallS callexits = new IRLCallS(callexit);
        
        
        //structure the method now
        act.head.next = calls;
        calls.next = callexits;
        
        
        
        return act;
    }
    
    public IRLActivation getOutOfBoundsException(Environment en){
        return getGenericException(JUMP_OBE,"\"*****RUNTIME ERROR***** Array out of Bounds access in method %s\"",en);
    }
    
    public IRLActivation getNoReturnException(Environment en){
         return getGenericException(JUMP_NRE,"\"*****RUNTIME ERROR***** No return value in method %s\"",en);
    }
    
    public String getOutofBoundsTemplate(int size,String where,IRLLabel method){
       
        //choose to write the code out instead of including a STM
        String ret = "";
        
        IRLLabel falsel = new IRLLabel("._falseOBE" + ++number);
        IRLLabel truel = new IRLLabel("._trueOBE" + number);
        IRLLabel jump = new IRLLabel("._jumpOBE" + number);
        
        CGTranslate trans = new CGTranslate();
        ret += trans.cmpCode("$" + String.valueOf(size), where);  //compare
        ret += trans.jumpCode(">=", falsel.name, true);  //jump
        ret += truel.name + ":\n";
        ret += trans.pushCode("$"+method.name);
        ret += trans.callCode("_" + JUMP_OBE); //no need to clear parameters as anyway it is going to exit
        ret += trans.jumpCode("jump", jump.name, false);
        ret += falsel.name + ":\n";
        ret += jump.name + ":\n";

        return ret;
                
    }
    
    public String getNoReturnTemplate(String methodName,IRLLabel label){
        
        //choose to write the code out instead of including a STM
        String ret = "";
        
        CGTranslate trans = new CGTranslate();
        ret += "." + methodName + "NRE:\n";
        ret += trans.pushCode("$"+label.name);
        ret += trans.callCode("_" + JUMP_NRE);
        
        return ret;
    }
    
    
}

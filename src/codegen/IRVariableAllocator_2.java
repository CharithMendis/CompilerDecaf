/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ir.low.IRLContainer;
import ir.low.IRLTemp;
import java.util.ArrayList;
import java.util.HashMap;
import semantic.symbol.Descriptor;
import semantic.symbol.Environment;
import semantic.symbol.MethodDescriptor;
import semantic.symbol.VariableDescriptor;

/**
 *
 * @author Charith
 */
public class IRVariableAllocator_2 {
    
    Environment top;
    IRLContainer irl;
    
    public static final int OFFSET = 2;
    int maximum;
    
    boolean debug;
   

    public IRVariableAllocator_2(IRLContainer irl,boolean debug) {
        this.top = irl.top;
        this.irl = irl;
        this.debug = debug;
    }
    
    public void traverse(){
        HashMap<String,Descriptor> symbol = top.symbolTable;
        
        Object[] all = symbol.keySet().toArray();
        
        if(debug){
            System.out.println("***********variable allocator**************");
        }
        
        for(int i=0;i<all.length;i++){
            Descriptor d = symbol.get((String)all[i]);
            if(d.getClass() == MethodDescriptor.class){
                maximum = 0;
                MethodDescriptor m = (MethodDescriptor)d;
                if(debug){
                    System.out.println(m.name);
                }
                traverseEnv(m.env,0,m.parameters,1);
                irl.getActivation((String)all[i]).localSize = maximum;
            }
        }
    }
    
    void keepTab(int t){
        for(int i=0;i<t;i++){
            System.out.print("\t");
        }
    }
    
    
    void traverseEnv(Environment env,int reg,ArrayList<VariableDescriptor> des,int t){
        HashMap<String,Descriptor> symbol = env.symbolTable;
        Object[] all = symbol.keySet().toArray();
        for(int i=0;i<all.length;i++){
            if(debug){
                keepTab(t);
            }
            VariableDescriptor vdes = (VariableDescriptor)symbol.get((String)all[i]);
            if(vdes.kind == VariableDescriptor.PARA){
               int para = des.lastIndexOf(vdes);
               IRLTemp temp = new IRLTemp();
               temp.loc = -(OFFSET + para);
               vdes.loc = temp;
               if(debug){
                   System.out.println( vdes.name + " : " + vdes.loc.getRegister());
               }
            }
            else{
               IRLTemp temp = new IRLTemp();
               reg = reg + 1;
               temp.loc = (reg) + IRLTemp.REGCNT;
               vdes.loc = temp;
               if(debug){
                    System.out.println( vdes.name + " : " + vdes.loc.getRegister());
               }
            }
        }
        
        for(int i=0;i<env.next.size();i++){
            traverseEnv(env.next.get(i),reg,des,t+1);
        }
        
        if(reg > maximum){
            maximum = reg;
        }
        
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.low;

import codegen.VisitorIR;
import java.util.ArrayList;
import semantic.symbol.Environment;
import semantic.symbol.FieldDescriptor;

/**
 *
 * @author Charith
 */
public class IRLContainer extends IRL{
    
    public Environment top;
    ArrayList<FieldDescriptor> fields;
    ArrayList<IRLActivation> activations;

    public IRLContainer(Environment top) {
        this.top = top;
        this.activations = new ArrayList();
        this.fields = new ArrayList();
    }
    
    public void addActivation(IRLActivation act){
        activations.add(act);
    }
    
    public IRLActivation getActivation(int i){
        return activations.get(i);
    }
    
    public int getActivationCount(){
        return activations.size();
    }
    
    public IRLActivation getActivation(String val){
        for(int i=0;i<activations.size();i++){
            if(activations.get(i).name.name.equals("_" + val)){
                return activations.get(i);
            }
        }
        return null;
    } 
    
    public void addField(FieldDescriptor f){
        fields.add(f);
    }
    
    public FieldDescriptor getField(int i){
        return fields.get(i);
    }
    
    public int getFieldCount(){
        return fields.size();
    }
    
    
    public Object accept(VisitorIR v,Object o) throws Exception{
        return v.visit(this,o);
    }
    
    
    
}

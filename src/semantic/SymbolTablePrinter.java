/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic;

import java.util.HashMap;
import java.util.Set;
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.Descriptor;
import semantic.symbol.Environment;
import semantic.symbol.MethodDescriptor;
import semantic.symbol.VariableDescriptor;

/**
 *
 * @author Charith
 */
public class SymbolTablePrinter {
    
    Environment en;
    
    public SymbolTablePrinter(Environment en){
        this.en = en; //should give the top environment
    }
    
    public void print(){
        //special print to handle the top layer
        Set<String> keys = en.symbolTable.keySet();
        String[] symbols = keys.toArray(new String[0]);
        for(int i=0;i<symbols.length;i++){
            Descriptor des = en.symbolTable.get(symbols[i]);
            
            if(des.getClass() == MethodDescriptor.class){
                ((MethodDescriptor)des).print();
                keepTab(1);
                System.out.println("locals:");
                print(((MethodDescriptor)des).env,2);
                 
            }
            else if(des.getClass() == VariableDescriptor.class){
                ((VariableDescriptor)des).print();
            }
            else{
                ((ArrayDescriptor)des).print();   
            }
            
        }
    }
    
    void keepTab(int t){
        for(int i=0;i<t;i++){
            System.out.print("  ");
        }
    }
    
    public void print(Environment en,int level){
        
        print(en.symbolTable,level);
        
        for(int i=0;i<en.next.size();i++){
            print(en.next.get(i),level + 1);

        }

    }
    
    public void print(HashMap<String,Descriptor> des,int t){
        
        Set<String> keys = des.keySet();
        String[] symbols = keys.toArray(new String[0]);
        for(int i=0;i<symbols.length;i++){
            Descriptor d = des.get(symbols[i]);
            Class c = d.getClass();
            c.cast(d);
            keepTab(t);
            d.print();
        }
    }
    
    
}

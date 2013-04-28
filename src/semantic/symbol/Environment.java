/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic.symbol;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Charith
 */
//functional type of symbol table maintained; persistent



public class Environment {
    
    public Environment prev;
    public ArrayList<Environment> next; //an environment can have multiple environments
    public HashMap<String, Descriptor> symbolTable;
    
    public Environment(Environment prev, HashMap<String, Descriptor> symbolTable){
        this.prev = prev;
        this.symbolTable = new HashMap();
        if(symbolTable != null){
            this.symbolTable.putAll(symbolTable);
        }
        this.next = new ArrayList();
    }
    
    public Environment(Environment prev){
        this.prev = prev;
        this.symbolTable = new HashMap();
        this.next = new ArrayList();
    }
    
    public void put(String name, Descriptor des){
        symbolTable.put(name,des);   
    }
    
    public boolean lookup(String name){
        return symbolTable.containsKey(name);
    }
    
    public Descriptor get(String name){
        for(Environment e=this;e!=null;e=e.prev){
            if(e.symbolTable.containsKey(name)){
                return e.symbolTable.get(name); 
            }
        }
        return null;
    }
    
    public void addEnv(Environment e){
        next.add(e);
    }
    
    
    
}

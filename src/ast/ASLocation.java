/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import ir.high.IRLoad;
import ir.high.IRStore;

/**
 *
 * @author Charith
 */
public abstract class ASLocation extends ASExpr{
    
    public String name;
    
    public IRStore store;
    public IRLoad load;
    public boolean isStore;
    
}

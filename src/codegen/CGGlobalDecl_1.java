/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ir.low.IRLContainer;
import java.io.BufferedWriter;
import semantic.symbol.ArrayDescriptor;
import semantic.symbol.FieldDescriptor;

/**
 *
 * @author Charith
 */
public class CGGlobalDecl_1 implements VisitorIR{

    BufferedWriter buf;

    public CGGlobalDecl_1(BufferedWriter buf){
        this.buf = buf;
    }
    
    void printInitial() throws Exception{
        buf.write(".section .data");
        buf.newLine();
    }
    
    @Override
    public void visit(IRLContainer ir) throws Exception{
        printInitial();
        int amount = ir.getFieldCount();
        for(int i=0;i<amount;i++){
            buf.write("\t .lcomm ");
            FieldDescriptor f = ir.getField(i);
            buf.write(f.name + " , ");
            if(f.getClass() == ArrayDescriptor.class){
                buf.write( '$' + String.valueOf(((ArrayDescriptor)f).size*4) );
            }
            else{
                buf.write("$4");
            }
            buf.newLine();
        }
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ir.low.STRING;
import java.io.BufferedWriter;
import java.util.ArrayList;

/**
 *
 * @author Charith
 */
public class CGString_2 {
    
    BufferedWriter buf;
    ArrayList<STRING> strings;

    public CGString_2(BufferedWriter buf, ArrayList<STRING> strings) {
        this.buf = buf;
        this.strings = strings;
    }
    
    public void print() throws Exception{
        for(int i=0;i<strings.size();i++){
            STRING now = strings.get(i);
            buf.write(now.label.name + ": .ascii ");
            String[] split = now.value.split("\"");
            buf.write("\"" + split[1] + "\\0\"");
            buf.newLine();
        }
    }
    
    
    
    
}

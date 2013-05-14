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
            String s = now.value;
            s = s.substring(0, s.length()-1);
            s = s.concat("\\0\"");
            buf.write(s);
            buf.newLine();
        }
    }
    
    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author Charith
 */
public class ASType {
    public int type;
    public String stringType;
    
    public static final int INT = 1;
    public static final int BOOLEAN = 2;
    public static final int VOID = 3;
    public static final int ERROR = 4;

    public ASType(String type) {
        this.stringType = type;
        switch (type) {
            case "int":
                this.type = INT;
                break;
            case "boolean":
                this.type = BOOLEAN;
                break;
            case "void":
                this.type = VOID;
                break;
            default:
                this.type = ERROR;
                break;
        }

    }
    
    
}

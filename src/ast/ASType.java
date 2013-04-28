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
    public ASType element;
    
    public static final int INT             = 1;
    public static final int BOOLEAN         = 2;
    public static final int INTARRAY        = 3;
    public static final int BOOLEANARRAY    = 3;
    public static final int VOID            = 5;
    public static final int ERROR           = 6;

    public ASType(String type) {
        this.element = null;
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
            case "intarray":
                this.type = INTARRAY;
                this.element = new ASType("int");
                break;
            case "booleanarray":
                this.type = BOOLEANARRAY;
                this.element = new ASType("boolean");
                break;
            default:
                this.type = ERROR;
                break;
        }

    }
    
    
}

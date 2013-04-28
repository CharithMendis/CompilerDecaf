/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic;

/**
 *
 * @author Charith
 */
public class SemanticErrorPrint {
    
    
    public static final String METHOD = "method";
    public static final String VARIABLE = "variable";
    
    public String filename;
    
    public SemanticErrorPrint(String filename){
        this.filename = filename;
    }
    
    private void printFirst(int line,int column){
        System.out.print("ERROR in \'" + filename + "\' at " + line + ":" + column + " - ");
    }
    
    public void printDuplicateError(String duplicate,String type,int line,int column){
        printFirst(line,column);
        System.out.println(type + " \'" + duplicate + "\' was declared earlier in this scope");
    }
    
    public void printArrayInitError(String array,int line,int column){
        printFirst(line,column);
        System.out.println(array + "\' index should be greater than zero");
    }
    
    public void printNotDeclared(String name,String type,int line,int column){
        printFirst(line,column);
        System.out.println(type + " \'" + name + "\' was never declared but used");
    }
    
    public void printNoReturnVal(String name,String type,int line,int column){
        printFirst(line,column);
        System.out.println(type + " \'" + name + "\' does not have a return value");
    }
    
    public void printTypeErrorMethod(String name,String parameter,String type,String shouldbe,int line,int column){
         printFirst(line,column);
         System.out.println(type + " \'" + name + "\' parameter \'" + parameter + "\' should be of type " + shouldbe);
    }
    
    public void printNotEnoughParameters(String name,int line,int column){
        printFirst(line,column);
        System.out.println(METHOD + " \'" + name + "\' parameter number mismatch" );
    }
    
    public void printTypeErrorVariable(String shouldbe, String actual,int line,int column){
        printFirst(line,column);
        System.out.println("type of expression should be " + shouldbe + " but is " + actual);
    }
    
    public void printTypeErrorVariable(String lhs,String rhs,String op,int line,int column){
        printFirst(line,column);
        System.out.println("cannot apply " + op + " to types " + lhs +" and " + rhs);
    }
    
    public void printNoReturnError(String name,int line,int column){
        printFirst(line,column);
        System.out.println(METHOD + " \'" + name + "\' cannot return anything");
    }
    
    public void printReturnTypeError(String name,String shouldbe,int line,int column){
        printFirst(line,column);
        System.out.println(METHOD + " \'" + name + "\' must return type " + shouldbe);
    }
    
    public void printIfError(String type,int line,int column){
        printFirst(line,column);
        System.out.println("the expression of the if statement should be boolean, but is " + type);
    }
    
    public void printForError(String t1, String t2,int line,int column){
         printFirst(line,column);
         System.out.println("the expressions of the for statement should be int,int , but is " + t1 + "," + t2);
    }
    
    public void printBreakOrContError(String what,int line,int column){
        printFirst(line,column);
        System.out.println(what + " should be inside a for statement" );
    }
    
    public void printArrayLocationError(String name,int line,int column){
        printFirst(line,column);
        System.out.println("the expression of array " + name + " should be of type int");
    }
    
    public void printArrayTypeError(String name,int line,int column){
        printFirst(line,column);
        System.out.println("the variable " + name + " should be of type array");
    }
    
    public void printMainError(int line,int column){
        printFirst(line,column);
        System.out.println("the main method should not have any parameters");
    }
    
    
}

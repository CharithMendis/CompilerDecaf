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
    
    public void printDuplicateError(String duplicate,String type){
        System.out.println("ERROR: " + type + " \'" + duplicate + "\' was declared earlier in this scope");
    }
    
    public void printArrayInitError(String array){
        System.out.println("ERROR: \'" + array + "\' index should be greater than zero");
    }
    
    public void printNotDeclared(String name,String type){
        System.out.println("ERROR: " + type + " \'" + name + "\' was never declared but used");
    }
    
    public void printNoReturnVal(String name,String type){
        System.out.println("ERROR: " + type + " \'" + name + "\' does not have a return value");
    }
    
    public void printTypeErrorMethod(String name,String parameter,String type,String shouldbe){
         System.out.println("ERROR: " + type + " \'" + name + "\' parameter \'" + parameter + "\' should be of type " + shouldbe);
    }
    
    public void printNotEnoughParameters(String name){
        System.out.println("ERROR: " + METHOD + " \'" + name + "\' parameter number mismatch" );
    }
    
    public void printTypeErrorVariable(String shouldbe, String actual){
        System.out.println("ERROR: type of expression should be " + shouldbe + " but is " + actual);
    }
    
    public void printTypeErrorVariable(String lhs,String rhs,String op){
        System.out.println("ERROR: cannot apply " + op + " to types " + lhs +" and " + rhs);
    }
    
    public void printNoReturnError(String name){
        System.out.println("ERROR: " + METHOD + " \'" + name + "\' cannot return anything");
    }
    
    public void printReturnTypeError(String name,String shouldbe){
        System.out.println("ERROR: " + METHOD + " \'" + name + "\' must return type " + shouldbe);
    }
    
    public void printIfError(String type){
        System.out.println("ERROR: the expression of the if statement should be boolean, but is " + type);
    }
    
    public void printForError(String t1, String t2){
         System.out.println("ERROR: the expressions of the for statement should be int,int , but is " + t1 + "," + t2);
    }
    
    public void printBreakOrContError(String what){
        System.out.println("ERROR:  " + what + " should be inside a for statement" );
    }
    
    public void printArrayLocationError(String name){
        System.out.println("ERROR: the expression of array " + name + " should be of type int");
    }
    
    public void printArrayTypeError(String name){
        System.out.println("ERROR: the variable " + name + " should be of type array");
    }
    
    public void printMainError(){
        System.out.println("ERROR: the main method should not have any parameters");
    }
    
    
}

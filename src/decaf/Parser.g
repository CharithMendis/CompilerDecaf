header {package decaf;}

options
{
  mangleLiteralPrefix = "TK_";
  language="Java";
  
}

class DecafParser extends Parser;
options
{
  importVocab=DecafScanner;
  k=3;
  buildAST=false;
  defaultErrorHandler=false;
}

program : TK_class TK_Program LCURLY (field_decl)* (method_decl)* RCURLY;


field_decl : type (ID | (ID LBRAC INT_VAL RBRAC))(COMMA (ID | (ID LBRAC INT_VAL RBRAC)))* SEMI!;
method_decl : (type|TK_void) ID LPARA (var)* RPARA block;
var_decl : var SEMI! ;

block : LCURLY (var_decl)* (statement)* RCURLY;

statement : location assign_op expr SEMI!
          | method_call SEMI! 
          | TK_if LPARA expr RPARA block (TK_else block)*
          | TK_for ID ASSIGN expr COMMA expr block
          | TK_return (expr)* SEMI!
          | TK_break SEMI!
          | TK_continue SEMI!
          | block ;
          
method_call : method_name LPARA ((expr)(COMMA expr)*)* RPARA
            | TK_callout LPARA STRING (COMMA (callout_arg)(COMMA callout_arg)*)? RPARA
            ;

//handle precedance
expr  : (pre_method_location | literal | (MINUS expr) | (UNARY_NOT expr) | (LPARA expr RPARA) ) expr_b;
     
     
expr_b : bin_op (pre_method_location | literal | (MINUS expr) | (UNARY_NOT expr) | (LPARA expr RPARA) )  expr_b
       |
       ;

pre_method_location : (method_name LPARA) => method_call
                    | location ;

callout_arg : expr | STRING;

protected bin_op : arith_op | rel_op | eq_op | cond_op;
protected arith_op : PLUS | MINUS | MULT | DIV | MOD;
protected rel_op : LT | GT | GE | LE;
protected eq_op : EQUAL | NEQ;
protected cond_op : LOGICAL_AND | LOGICAL_OR ;
protected literal : INT_VAL | CHAR | bool_literal;
protected bool_literal : TK_true | TK_false;
protected type : ( TK_int | TK_boolean);
protected var : (type ID)(COMMA type ID)* ;
protected assign_op : ASSIGN | INC_ASSIGN | DEC_ASSIGN ;
protected method_name : ID;
protected location : ID 
                  | ID LBRAC expr RBRAC;




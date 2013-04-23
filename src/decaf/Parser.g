header {package decaf;}


options
{
  mangleLiteralPrefix = "TK_";
  language="Java";
  
}

{
    import ast.*;

}

class DecafParser extends Parser;
options
{
  importVocab=DecafScanner;
  k=3;
  buildAST=false;
  defaultErrorHandler=false;
}

//done
program returns [ASProgram p] 
                    {
                        p = new ASProgram();
                    }
                    : TK_class TK_Program LCURLY 
                     (field_decl[p])* (method_decl[p])* RCURLY;


field_decl [ASProgram p]: type (ID | (ID LBRAC INT_VAL RBRAC))(COMMA (ID | (ID LBRAC INT_VAL RBRAC)))* SEMI!;
method_decl [ASProgram p]: (type|TK_void) ID LPARA (var)* RPARA block;


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
          
method_call returns [ASMethodCall method]
            {
                String m;
                method = null;
            }
            : 
                m=method_name 
                {
                    method = new ASNormalCall(m); 
                } 
                LPARA ((expr {((ASNormalCall)method).addArgument(null);})(COMMA expr {((ASNormalCall)method).addArgument(null);})*)* RPARA  
                
            |   
                TK_callout LPARA n:STRING 
                {
                    method = new ASLibraryCall(n.getText()); 
                }
                (COMMA (callout_arg {((ASLibraryCall)method).addArgument(null);} )(COMMA callout_arg {((ASLibraryCall)method).addArgument(null);} )*)? RPARA
                
            ;

//handle precedance
terms returns [ASExpr expr]
    {
        expr = null;
    }
    : expr = pre_method_location 
    | expr = literal 
    | (LPARA expr RPARA) {expr = null;} 
    ;

expr : andexpr ((LOGICAL_OR) expr)?;

andexpr : eqexpr ((LOGICAL_AND) andexpr)?;

eqexpr : relexpr ((EQUAL|NEQ) eqexpr)?;

relexpr : pexpr ((GE|GT|LE|LT) relexpr)?;

pexpr : mexpr (PLUS pexpr)? ;

//done - important debug (made left associative) additionally had to consider precedance
mexpr : mmexpr (MINUS mexpr)? ;


//done - right associative - no need to change
mmexpr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
                String op="";
            }
            : l=dexpr ((MULT {op="*";}|MOD {op="%";}) r=mmexpr)?
                {
                    if(r!=null){ //middle of the expr
                        expr = new ASBinaryExpr(op,l,r);
                    }
                    else{
                        expr = l;
                    }
                }
            ;

//done - important debug (made left associative)
dexpr returns [ASExpr expr]
            {   
                expr = null;
                ASExpr l=null,r=null;
            }
            : l=unot (DIV r=dexpr)? 
              {
                    //System.out.println(l);
                    //System.out.println(r);
                    if(r!=null){ //middle of the expr
                        if(r.getClass() == ASBinaryExpr.class && !r.isParan){
                            //we can rearrange the tree now
                            //need to check for precedance ; as this is the highest among binary no need here
                            ASBinaryExpr rtemp = ((ASBinaryExpr)r);
                            ASExpr newRhs = rtemp.lhs;
                            ASBinaryExpr newEx = new ASBinaryExpr("/",l,newRhs);
                            expr = new ASBinaryExpr(rtemp.stringop,newEx,rtemp.rhs);
                        }
                        else if(r instanceof ASLiteral){
                            expr = new ASBinaryExpr("/",l,r);
                        }
                    }
                    else{   //when the last token is read
                        expr = l;
                    }
                    //System.out.println(expr);
              };

//done
unot returns [ASExpr expr]
            {
                expr = null;
                ASExpr r;
            }
            : UNARY_NOT r=unot {expr = new ASUnaryExpr("!",r);}| r=uminus {expr = r;} ;

//done
uminus returns [ASExpr expr]
            {
                expr = null;
                ASExpr r;
            }
            : 
            MINUS r=uminus {expr = new ASUnaryExpr("-",r);}| r=terms {expr = r;};


pre_method_location returns [ASExpr expr]
             {
                expr=null;
                ASMethodCall r;
             }
             : (method_name LPARA) => r=method_call {expr = new ASMethodCallE(r);}
             | expr=location ;

callout_arg : expr | STRING;

//protected bin_op : arith_op | rel_op | eq_op | cond_op;
//protected arith_op  : PLUS | MINUS | MULT | DIV | MOD ;
//protected rel_op : LT | GT | GE | LE;
//protected eq_op : EQUAL | NEQ;
//protected cond_op : LOGICAL_AND | LOGICAL_OR ;

protected literal returns [ASLiteral l]
        {   
            l= null;
        }
        : i:INT_VAL {l = new ASIntLiteral(i.getText());}
        | j:CHAR    {l = new ASCharLiteral(j.getText().toCharArray()[0]);}
        | k:TK_true {l = new ASBooleanLiteral(k.getText().equals("true"));} 
        | m:TK_false{l = new ASBooleanLiteral(m.getText().equals("false"));} ;


protected type returns [ASType t]
            {
                t=null;
            }
            : ( TK_int {t=new ASType("int");} | TK_boolean {t=new ASType("boolean");});


protected var : (type ID)(COMMA type ID)* ;
protected assign_op : ASSIGN | INC_ASSIGN | DEC_ASSIGN ;

//string of the method name
protected method_name returns [String id] 
                        {
                            id = ""; //initializing
                        }
                      : i:ID {id = i.getText();};

//ASlocation
protected location returns [ASLocation loc]
                  {
                        loc = null;
                  }
                  :i:ID {loc = new ASLocationVar(i.getText());}
                  |j:ID LBRAC expr RBRAC {loc = new ASLocationArray(j.getText(),null);};

//mpexpr : mdmexpr s1 ;

//s1 : (PLUS|MINUS) mpexpr s1 | ;

//mdmexpr :  unot s2 ;

//s2 : (MULT|DIV|MOD) mdmexpr s2 | ;


/*
class CalcTreeWalker extends TreeParser;

expr returns [float r]
{
    float a,b;
    r=0;
}
    :   #(PLUS a=expr b=expr)   {r = a+b;}
    |   #(STAR a=expr b=expr)   {r = a*b;}
    |   i:INT
        {r = (float)
         Integer.parseInt(i.getText());}
    ;*/



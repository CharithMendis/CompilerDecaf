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

{
    //common function
    ASExpr getExpr(String op,ASExpr l,ASExpr r){
        ASExpr expr;
        if(r!=null){
            expr = new ASBinaryExpr(op,l,r,l.line,l.column);
        }
        else{
            expr = l;
        }
        return expr;
    }

    private class IntLiteral{
        public int i;
        public int line;
        public int column;

        public IntLiteral(int i, int line,int col){
            this.i = i;
            this.line = line;
            this.column = col;
        }
    }
}
//done - line / col
program returns [ASProgram p] 
                    {
                        p = new ASProgram();
                    }
                    : TK_class i:TK_Program {p.line = i.getLine(); p.column = i.getColumn();}LCURLY 
                     (field_decl[p])* (method_decl[p])* RCURLY;
//done - line / col
field_decl [ASProgram p]
            {
                ASType t;
                IntLiteral a1=null,a2=null;
            }
            : 
            t=type (id1:ID {p.addFieldDecl(new ASVariable(t,id1.getText(),id1.getLine(),id1.getColumn()));} | 
                        (id2:ID LBRAC a1=int_literal RBRAC {p.addFieldDecl(new ASArray(new ASType(t.stringType + "array"),id2.getText(),a1.i,id2.getLine(),id2.getColumn()));}))
                   (COMMA (id3:ID {p.addFieldDecl(new ASVariable(t,id3.getText(),id3.getLine(),id3.getColumn()));} | 
                        (id4:ID LBRAC a2=int_literal RBRAC{p.addFieldDecl(new ASArray(new ASType(t.stringType + "array"),id4.getText(),a2.i,id4.getLine(),id4.getColumn()));})))* SEMI!;

protected int_literal returns [IntLiteral lit]
        {
            lit = null;
            int y = 0;
        }
        : i:INT_VAL
        {
            if(i.getText().startsWith("0x")){
                y = Integer.parseInt(i.getText().substring(2),16);
            }
            else{
                y = Integer.parseInt(i.getText());
            }
            lit = new IntLiteral(y,i.getLine(),i.getColumn());
        };

//done - line / col
method_decl [ASProgram p] 
            {
                ASType t=null;
                ASMethodDecl dec=null;
                ASBlock b=null;
            }
            : (t=type|k:TK_void {t = new ASType(k.getText());}) 
               n:ID {dec = new ASMethodDecl(t,n.getText(),n.getLine(),n.getColumn());} 
                                                    LPARA (varparameter[dec])? RPARA b=block
               {
                    dec.addBlock(b);
                    p.addMethodDecl(dec);
               }
            ;

//done - line / col
protected varparameter [ASMethodDecl d] 
            {
                ASType t1,t2;
            }
            : (t1=type id1:ID {
                     d.addParameters(new ASVariable(t1,id1.getText(),id1.getLine(),id1.getColumn()));
                        
               })
              (COMMA t2=type id2:ID {
                      d.addParameters(new ASVariable(t2,id2.getText(),id2.getLine(),id2.getColumn()));

               })* ;

//done - line / col
protected var [ASBlock b] 
            {
                ASType t1,t2;
            }
            : (t1=type id1:ID {   
                      b.addVar(new ASVariable(t1,id1.getText(),id1.getLine(),id1.getColumn()));
               })
              (COMMA id2:ID {
                        
                      b.addVar(new ASVariable(t1,id2.getText(),id2.getLine(),id2.getColumn()));
               })* ;

//done
var_decl [ASBlock b] : var[b] SEMI! ;

//done
block returns [ASBlock b]
    {
        b = new ASBlock();
    }
    : l:LCURLY {b.line = l.getLine(); b.column = l.getColumn();} 
        (var_decl[b])* (statement[b])* RCURLY ;

//done - line / col
statement [ASBlock b]
          {
            ASStatement stat=null;
            //assignement
            ASLocation loc;
            String op;
            ASExpr ex;
            //method call
            ASMethodCall m;
            //if
            ASExpr ifex;
            ASBlock ifb;
            ASBlock elseb=null;
            //for
            ASExpr start;
            ASExpr end;
            ASBlock forb;
            //return
            ASExpr retex=null;
            //block
            ASBlock bb;
          }
          : 
          loc=location op=assign_op ex=expr SEMI! {stat = new ASAssignment(op,loc,ex,loc.line,loc.column); b.addStatement(stat);}
          | m=method_call SEMI! {stat = new ASMethodCallS(m,m.line,m.column); b.addStatement(stat);}
          | l1:TK_if LPARA ifex=expr RPARA ifb=block (TK_else elseb=block)? {stat = new ASIf(ifex,ifb,elseb,l1.getLine(),l1.getColumn()); b.addStatement(stat);}
          | l2:TK_for v:ID ASSIGN start=expr COMMA end=expr forb=block {stat = new ASFor(v.getText(),start,end,forb,l2.getLine(),l2.getColumn()); b.addStatement(stat); } 
          | l3:TK_return (retex=expr)? SEMI! {stat = new ASReturn(retex,l3.getLine(),l3.getColumn()); b.addStatement(stat);}
          | l4:TK_break SEMI! {stat = new ASBreak(l4.getLine(),l4.getColumn()); b.addStatement(stat);}
          | l5:TK_continue SEMI! {stat = new ASContinue(l5.getLine(),l5.getColumn()); b.addStatement(stat);}
          | bb=block {stat=bb; b.addStatement(stat);}        //no need for line here  
          ;
          
//done - line / col
method_call returns [ASMethodCall method]
            {
                //String m;
                method = null;
                ASExpr e;
                ASCalloutArg arg;
            }
            : 
                //m=method_name
                m:ID
                {
                    method = new ASNormalCall(m.getText(),m.getLine(),m.getColumn()); 
                } 
                LPARA ((e=expr {((ASNormalCall)method).addArgument(e);})(COMMA e=expr {((ASNormalCall)method).addArgument(e);})*)* RPARA  
                
            |   
                l:TK_callout LPARA n:STRING 
                {
                    method = new ASLibraryCall(n.getText(),l.getLine(),l.getColumn()); 
                }
                (COMMA (arg=callout_arg {((ASLibraryCall)method).addArgument(arg);} )(COMMA arg=callout_arg {((ASLibraryCall)method).addArgument(arg);} )*)? RPARA
                
            ;
//done - line / col
callout_arg returns [ASCalloutArg arg]
        {
            arg = null;
            ASExpr e;
        }
        : e=expr {arg=e;} | s:STRING {arg = new ASStringLiteral(s.getText(),s.getLine(),s.getColumn());};

//handle precedence and  done - not needed
terms returns [ASExpr expr]
    {
        expr = null;
    }
    : expr = pre_method_location 
    | expr = literal 
    | (LPARA expr = expr RPARA) {expr.isParan=true;} 
    ;

//done - line / col
pre_method_location returns [ASExpr expr]
             {
                expr=null;
                ASMethodCall r;
             }
             : ( (TK_callout|ID) LPARA) => r=method_call {expr = new ASMethodCallE(r,r.line,r.column);}
             | expr=location ;

//done
expr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
            }
            : l=andexpr ((LOGICAL_OR) r=expr)?
            {
                 expr = getExpr("||",l,r);
            }
            ;

//done
andexpr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
            }
            : l=eqexpr ((LOGICAL_AND) r=andexpr)?
            {
                expr = getExpr("&&",l,r);
            }
            ;
//done
eqexpr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
                String op="";
            }
            : l=relexpr ((EQUAL{op="==";}|NEQ{op="!=";}) r=eqexpr)?
            {
                expr = getExpr(op,l,r);
            }
            ;

//done
relexpr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
                String op="";
            }
            : l=pexpr ((GE{op=">=";}|GT{op=">";}|LE{op="<=";}|LT{op="<";}) r=relexpr)?
            {
                expr = getExpr(op,l,r);
            }
            ;

//done
pexpr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
            }
            : l=mexpr (PLUS r=pexpr)? 
            {
                expr = getExpr("+",l,r);
            }    
            ;

//done - important debug (made left associative) additionally had to consider precedence
mexpr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
            }
            : l=mmexpr (MINUS r=mexpr)? 
            {
                if(r!=null){ //middle of the expr

                        ASExpr rtemp = r;
                        ASExpr prev = null;
                        while(rtemp.getClass()==ASBinaryExpr.class && !rtemp.isParan
                                && ((ASBinaryExpr)rtemp).operator != ASBinaryExpr.MOD 
                                && ((ASBinaryExpr)rtemp).operator != ASBinaryExpr.MULT 
                                && ((ASBinaryExpr)rtemp).operator != ASBinaryExpr.DIV){
                            prev = rtemp;
                            rtemp = ((ASBinaryExpr)rtemp).lhs;
                        }

                        ASBinaryExpr newNode = new ASBinaryExpr("-",l,rtemp,l.line,l.column);
                        if(prev != null){
                            ((ASBinaryExpr)prev).lhs = newNode;
                            expr = r;
                        }
                        else{
                            expr = new ASBinaryExpr("-",l,r,l.line,l.column); 
                        }
                        
                    }
                    else{   //when the last token is read
                        expr = l;
                    }
                    //System.out.println(expr);
            }
            ;


//done - right associative - no need to change
mmexpr returns [ASExpr expr]
            {
                expr = null;
                ASExpr l=null,r=null;
                String op="";
            }
            : l=dexpr ((MULT {op="*";}|MOD {op="%";}) r=mmexpr)?
                {
                    expr = getExpr(op,l,r);
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
                    if(r!=null){ //middle of the expr
                        
                        ASExpr rtemp = r;
                        ASExpr prev = null;
                        while(rtemp.getClass()==ASBinaryExpr.class && !rtemp.isParan){
                            prev = rtemp;
                            rtemp = ((ASBinaryExpr)rtemp).lhs;
                        }

                        ASBinaryExpr newNode = new ASBinaryExpr("/",l,rtemp,l.line,l.column);
                        if(prev != null){
                            ((ASBinaryExpr)prev).lhs = newNode;
                            expr = r;
                        }
                        else{
                            expr = new ASBinaryExpr("/",l,r,l.line,l.column); 
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
            : l:UNARY_NOT r=unot {expr = new ASUnaryExpr("!",r,l.getLine(),l.getColumn());}| r=uminus {expr = r;} ;

//done
uminus returns [ASExpr expr]
            {
                expr = null;
                ASExpr r;
            }
            : 
            l:MINUS r=uminus {expr = new ASUnaryExpr("-",r,l.getLine(),l.getColumn());}| r=terms {expr = r;};






//protected bin_op : arith_op | rel_op | eq_op | cond_op;
//protected arith_op  : PLUS | MINUS | MULT | DIV | MOD ;
//protected rel_op : LT | GT | GE | LE;
//protected eq_op : EQUAL | NEQ;
//protected cond_op : LOGICAL_AND | LOGICAL_OR ;

//done
protected literal returns [ASLiteral l]
        {   
            l= null;
            IntLiteral val=null;
        }
        : val=int_literal {l = new ASIntLiteral(val.i,val.line,val.column);}
        | j:CHAR    {l = new ASCharLiteral(j.getText().toCharArray()[0],j.getLine(),j.getColumn());}
        | k:TK_true {l = new ASBooleanLiteral(k.getText().equals("true"),k.getLine(),k.getColumn());} 
        | m:TK_false{l = new ASBooleanLiteral(m.getText().equals("false"),m.getLine(),m.getColumn());} ;

//done
protected type returns [ASType t]
            {
                t=null;
            }
            : ( TK_int {t=new ASType("int");} | TK_boolean {t=new ASType("boolean");});




//done
protected assign_op returns [String s]
            {
                s="";
            }
            : i:ASSIGN {s=i.getText();} 
            | j:INC_ASSIGN {s=j.getText();}
            | k:DEC_ASSIGN {s=k.getText();};

//string of the method name
/*protected method_name returns [String id] 
                        {
                            id = ""; //initializing
                        }
                      : i:ID {id = i.getText();};*/

//ASlocation
protected location returns [ASLocation loc]
                  {
                        loc = null;
                        ASExpr ex;
                  }
                  :i:ID {loc = new ASLocationVar(i.getText(),i.getLine(),i.getColumn());}
                  |j:ID LBRAC ex=expr RBRAC {loc = new ASLocationArray(j.getText(),ex,j.getLine(),j.getColumn());};





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
}


program : TK_class TK_Program LCURLY (field)* RCURLY;

field : type (ID | (ID LBRAC INT RBRAC))(COMMA (ID | (ID LBRAC INT RBRAC)))* SEMI!;

protected type : ( TK_int | TK_boolean | TK_void );




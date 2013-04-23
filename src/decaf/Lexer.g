header {package decaf;}

options 
{
  mangleLiteralPrefix = "TK_";
  language="Java";
}



class DecafScanner extends Lexer;
options 
{
  k=2;
}

tokens 
{
  "class";
  "boolean"; 
  "break"; 
  "callout"; 
  "continue";
  "else";
  "false";
  "for";
  "if"; 
  "int"; 
  "return"; 
  "true"; 
  "void";
  "Program"; 
}

LCURLY : '{';
RCURLY : '}';

LPARA : '(' ;
RPARA : ')' ;

LBRAC : '[';
RBRAC : ']';

SEMI : ';';
COMMA : ','; //for certain commands

//arithmetic
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
MOD : '%';


//comparison
EQUAL : '=' '=';
NEQ : '!' '=';
GE : '>' '=';
LE : '<' '=';
GT : '>';
LT : '<';

//assigning
ASSIGN : '=';
INC_ASSIGN : "+=";
DEC_ASSIGN : "-="; 

//logical operations
LOGICAL_AND : "&&";
LOGICAL_OR  : "||";
UNARY_AND   : '&';
UNARY_OR    : '|'; 
UNARY_NOT   : '!';

ID  : ('_'|'a'..'z'|'A'..'Z')('_'|'a'..'z'|'A'..'Z'|'0'..'9')* ;

WS_ : ('\r' | '\t' | ' ' | '\n' {newline();} ) {_ttype = Token.SKIP;};

SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline();};

CHAR : '\'' (ESC|VALID) '\'';
STRING : '"' (ESC|VALID)* '"';
INT_VAL : HEXA | DEC;


protected ESC :  '\\' ('n'|'"'|'\''|'t'|'\\');
protected VALID : ('\u0020'..'\u0021' | '\u0023'..'\u0026' | '\u0028'..'\u005B' | '\u005D'..'\u007E');
protected HEXA : "0x" ('0'..'9' | 'a' .. 'f' | 'A' .. 'F')+;
protected DEC : ('0'..'9')+;



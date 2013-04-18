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

LCURLY : "{";
RCURLY : "}";

LPARA : "(" ;
RPARA : ")" ;

LBRAC : "[";
RBRAC : "]";

SEMI : ";";
COMMA : ","; //for certain commands

//arithmetic
ADDOP : PLUS | MINUS;
MULOP : MULT | DIV;
RSHIFT : ">>";
LSHIFT : "<<";

//comparisons
COMP_EQ : NEQ | EQUAL | GE | LE ;
COMP : GT | LT;

//assigning
ASSIGN : '=';
INC_ASSIGN : ('+' | '-')('='); 

//logical operations
LOGICAL : "&&" | "||";
UNARY : '&' | '|' | '!';

ID options { paraphrase = "an identifier"; } : 
  ('_'|'a'..'z'|'A'..'Z')('_'|'a'..'z'|'A'..'Z'|'0'..'9')*;

WS_ : ('\r' | '\t' | ' ' | '\n' {newline();}) {_ttype = Token.SKIP; };

SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline (); };

CHAR : '\'' (ESC|VALID) '\'';
STRING : '"' (ESC|VALID)* '"';
INT : HEXA | DEC;


protected ESC :  '\\' ('n'|'"'|'\''|'t'|'\\');
protected VALID : ('\u0020'..'\u0021' | '\u0023'..'\u0026' | '\u0028'..'\u005B' | '\u005D'..'\u007E');
protected HEXA : "0x" ('0'..'9' | 'a' .. 'f' | 'A' .. 'F')+;
protected DEC : ('0'..'9')+;

protected EQUAL : '=' '=';
protected NEQ : '!' '=';
protected GE : '>' '=';
protected LE : '<' '=';
protected GT : '>';
protected LT : '<';

protected PLUS : '+';
protected MINUS : '-';
protected MULT : '*';
protected DIV : '/';



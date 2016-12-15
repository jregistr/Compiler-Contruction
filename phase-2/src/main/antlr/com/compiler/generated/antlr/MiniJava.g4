grammar MiniJava;

prog :  mainClass classDecl*
       ;
mainClass
  :   'class' ID '{' 'public' 'static' 'void' 'main'
                '(' 'String' '[' ']' ID ')' '{' variableDeclaration* statement* '}' '}'
  ;
classDecl
  :   'class' ID '{' fieldDeclaration* methodDecl* '}'
        # baseClass
  |   'class' ID 'extends' ID '{' fieldDeclaration* methodDecl* '}'
        # childClass
  |   'case' 'class' ID '(' (caseProperty (',' caseProperty+)*)? ')' ';'
        # caseClassDecl
        ;
variableDeclaration : type ID ';'
        ;
fieldDeclaration : type ID ';'
        ;
methodDecl :
        'public' type ID '(' (methodParam (',' methodParam+)*)? ')'
        '{' variableDeclaration* statement* 'return' expr ';' '}'
        ;
methodParam : type ID
        ;
caseProperty: type ID
        ;
type  :   'int'
  |   'int' '[' ']'
  |   'boolean'
  |   ID
        ;
statement
  :   '{' statement* '}'
        # basicBlock
  |   'System.out.println' '(' expr ')' ';'
        # printToConsole
  |   ID '=' expr ';'
        # varDefinition
  |   ID '[' expr ']' '=' expr ';'
        # arrayDefinition
  |   'while' '(' expr ')' whileBlock
        # whileLoopHead
  |   'if' '(' expr ')' ifBlock 'else' elseBlock
        # ifStatement
  ;

whileBlock: statement;
ifBlock: statement;
elseBlock: statement;

expr
  : expr PLUS expr
  # plusExpression
  |   expr MINUS expr
  # subtractExpression
  |   expr MULT expr
  # multiplyExpression
  |   atom '[' expr ']'
  # arrayAccessExpression
  |   expr '.' 'length'
  # arrLenExpression
  |   expr '.' ID '(' ( expr ( ',' expr )* )? ')'
  # methodCallExpression
  | '(' expr ')'
  # parenExpr
  |   '!' expr
  # notExpr
  |   expr LESS_THAN expr
  # lessThanExpr
  |   expr GREAT_THAN expr
  # greaterThanExpr
  |   expr '&&' expr
  # andExpr
  |   atom
  # atomExpr
  ;

PLUS: '+';
MINUS: '-';
MULT: '*';
LESS_THAN: '<';
GREAT_THAN: '>';
AND: '&&';

atom :
    BOOLEAN_LIT
  # booleanLit
  | INT_LIT
  # intLiteral
  | ID
  # idLiteral
  | 'new' ID '(' ')'
  # constructorCall
  | 'this'
  # thisCall
  | 'new' 'int' '[' expr ']'
  # integerArr
  ;

ID        :   [a-zA-Z_][a-zA-Z0-9_]*;
INT_LIT       :   '0'..'9'+ ;
BOOLEAN_LIT   :   ('true' | 'false') ;
WS        :   [ \t\r\n]+ -> skip ;
COMMENT   : '/*' .*? '*/' -> skip ;
LINE_COMMENT
          : '//' .*? '\r'? '\n' -> skip;
THIS: (ID | 'this');
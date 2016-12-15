grammar MiniJava;

goal
    :   mainClass
        classDecl*
        EOF
    ;
mainClass
  :   'class' className '{' 'public' 'static' 'void' 'main'
                '(' 'String' '[' ']' ID ')' '{' statement* '}' '}'
  ;

className
  : ID
  ;

parentName
  : ID
  ;

classDecl
  :   'class' ID '{' fieldDeclaration* methodDecl* '}'
        # baseClass
  |   'class' ID 'extends' parentName '{' fieldDeclaration* methodDecl* '}'
        # childClass
        ;
variableDeclaration : type ID ';'
        | mutable='mutable' type ID ';'
        ;
fieldDeclaration : type ID ';'
        | mutable='mutable' type ID ';'
        ;
methodDecl :
        'public' type ID '(' (methodParam (',' methodParam+)*)? ')'
        '{' variableDeclaration* statement* 'return' expr ';' '}'
        ;
methodParam : type ID
        ;

type  :   'int'
  |   'int' '[' ']'
  |   'boolean'
  |   ID
        ;
statement
  :   'System.out.println' '(' expr ')' ';'
        # printToConsole
  |   ID '=' expr ';'
        # varDefinition
  |   ID '[' expr ']' '=' expr ';'
        # arrayDefinition
  |   'while' '(' expr ')' statement
        # whileLoopHead
  |   'if' '(' expr ')' statement 'else' statement
        # ifStatement
  ;

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
  INT_LIT
  # intLiteral
  | ID
  # idLiteral
  | 'new' ID '(' ')'
  # constructorCall
  | 'this'
  # thisCall
  | 'new' 'int' '[' expr ']'
  # integerArr
  | BOOLEAN_LIT
  # booleanLit
  ;

ID        :   [a-zA-Z_][a-zA-Z0-9_]*;
INT_LIT       :   '0'..'9'+ ;
BOOLEAN_LIT   :   ('true' | 'false') ;
WS        :   [ \t\r\n]+ -> skip ;
COMMENT   : '/*' .*? '*/' -> skip ;
LINE_COMMENT
          : '//' .*? '\r'? '\n' -> skip;
THIS: (ID | 'this');
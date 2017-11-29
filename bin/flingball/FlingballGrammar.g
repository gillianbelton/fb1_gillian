/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

@skip COMMENT {
    EXPRESSION ::= DEFINITION*;
    DEFINITION ::= BOARD | BALL | SQUARE_BUMPER | CIRCLE_BUMPER | TRIANGLE_BUMPER | ABSORBER | FIRE;
}

@skip WHITESPACE {
    BOARD ::= 'board' 'name' '=' NAME (G)? (F1)? (F2)?; 
    BALL ::= 'ball' 'name' '=' NAME 'x' '=' FLOAT 'y' '=' FLOAT 'xVelocity' '=' FLOAT 'yVelocity' '=' FLOAT; 
    SQUARE_BUMPER ::= 'squareBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
    CIRCLE_BUMPER ::= 'circleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
    TRIANGLE_BUMPER ::= 'triangleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?; 
    ABSORBER ::= 'absorber' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER 'width' '=' INTEGER 'height' '=' INTEGER;
    FIRE ::= 'fire' 'trigger' '=' NAME 'action' '=' NAME;
    G ::= 'gravity' '=' FLOAT;
    F1 ::= 'friction1' '=' FLOAT;
    F2 ::= 'friction2' '=' FLOAT;
}

WHITESPACE ::= [ \t\r\n]+;
INTEGER ::= [0-9]+;
FLOAT ::= '-'?([0-9]+ '.' [0-9]* | '.'? [0-9]+);
NAME ::= [A-Za-z_][A-Za-z_0-9]*;
COMMENT ::= '#' [^'\n']* '\n';
ORIENTATION ::= '0' | '90' | '180' | '270';

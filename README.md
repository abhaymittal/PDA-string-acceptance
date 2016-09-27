# Pushdown Automaton

### Author: Abhay Mittal

This program takes a pushdown automaton as input, implements it and then checks strings if they pass the test or not. The concepts are from the book __"Introduction to Automata Theory, Languages, and Computation"__ (2nd edition) by __John E. Hopcroft, Rajeev Motwani__ and __Jeffrey D. Ullman__.

It is possible to see the intermediate steps in the program.

###Steps to execute###
1. `java PDAImplement.java`
2. `javac PDAImplement`

###Input###
The 7 tuple describing the pushdown automaton tuple:

Q: Set of States
S: Input alphabet
SS: Stack alphabet
q0: Initial automaton state
z0: Initial stack symbol
F: set of final states
T: Transition table	

####Example Automaton Input####
```
// Formal push down automata for accepting {ww^R|w in (0+1)^R}
Q
 qo   q1 q2
-----------------------
S
0 1 @
------------------------
SS
0 1 zo
---------------------------
qo 
qo
------------------------
zo
zo
------------------------
F
q2
-----------------------
T 
qo 0 zo = qo 0.zo
qo 1 zo = qo 1.zo
qo 0 0 = qo 0.0
qo 0 1 = qo 0.1
qo 1 0 = qo 1.0
qo 1 1 = qo 1.1
qo @ zo = q1 zo
qo @ 0 = q1 0
qo @ 1 = q1 1
q1 0 0 = q1 @
q1 1 1 = q1 @
q1 @ zo = q2 zo
```
Also, you need to input strings which you need to check interactively.
###Output###
Whether the string is accepted or not

###Sample Run (For the example input)###
```
Enter the name of the input file
pda-evenPalin.txt
Q
qo   q1   q2   
-------------------------------------------------------------------------------

S
0   1   @   
-------------------------------------------------------------------------------

SS
0   1   zo   
-------------------------------------------------------------------------------

qo
qo

-------------------------------------------------------------------------------

zo
zo

-------------------------------------------------------------------------------

F

-------------------------------------------------------------------------------

T
qo  0 zo = qo 0.zo
qo  1 zo = qo 1.zo
qo  0  0 = qo 0.0
qo  0  1 = qo 0.1
qo  1  0 = qo 1.0
qo  1  1 = qo 1.1
qo  @ zo = q1 zo
qo  @  0 = q1  0
qo  @  1 = q1  1
q1  0  0 = q1  @
q1  1  1 = q1  @
q1  @ zo = q2 zo
Enter a string
110011
String is accepted
Press enter to continue
```
Input 2 for the above automaton
```
Enter a string
11011
String is rejected
Press enter to continue
```
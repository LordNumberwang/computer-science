//Fill.asm
/*
This program runs an infinite loop that listens to the
keyboard. When a key is pressed (any key), the program blackens the entire screen by writing
"black" in every pixel. 
When no key is pressed, the program clears the screen by writing "white" in
every pixel. You may choose to blacken and clear the screen in any spatial order (top-down,
bottom-up, spiral, etc.), as long as pressing a key continuously for long enough will result in a fully
blackened screen, and not pressing any key for long enough will result in a cleared screen.
*/

//read KBD register
(READ)
@KBD
D=M
@FILL
D;JGT

(EMPTY)
@idx
D=M
@SCREEN
A=D+A
M=0
@INCREMENT
0;JMP

(FILL)
@idx
D=M
@SCREEN
A=D+A
M=-1

(INCREMENT)
@idx
MD=M+1
@8192 //length of screen
D=D-A

@READ
D;JNE

(RESET)
@idx
M=0
@READ
0;JMP

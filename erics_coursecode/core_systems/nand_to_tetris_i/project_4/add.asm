//Add.asm: Adds the constants 2 and 3, and puts the result in R0 (recall that Ri refers to RAM[i]).
@2
D=A
@3
D=D+A
@R0
M=D
(END)
@END
0;JMP
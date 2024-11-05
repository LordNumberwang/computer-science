//Max.asm: Computes max(R0, R1) and puts the result in R2. Before executing, put some test values
//in R0 and R1.

//Get R0
@R0
D=M
@tmp
M=D //store tmp = R0

//JUMP (RETURNVAL) if R1 < R0 
@R1
D=D-M
@RETURNVAL
D;JGT

//Set tmp=R1 
@R1
D=M
@tmp
M=D

(RETURNVAL)
@tmp
D=M
@R2
M=D

(END)
@END
0;JMP
//Mult.asm
/*
The program computes the product R0 * R1 and stores the result in
R2 (RAM[2]). Assume that R0 ≥ 0, R1 ≥ 0, and R0 * R1 < 32768 (your program need not test these
www.nand2tetris.org / Copyright © Noam Nisan and Shimon Schocken

conditions). Your code should not change the values of R0 and R1. The supplied Mult.test script
and Mult.cmp compare file are designed to test your program on the CPU emulator, using some
representative R0 and R1 values.
*/

(INIT)
@R0
D=M
@num1
M=D //set @num1=R0
@R1
D=M
@num2
M=D //set @num2=R1
@mult
M=0 //init mult=0

(ADD)
@num1
D=M
@DONE
D;JLE //if done (num1)<=0, goto DONE

@num2
D=M
@mult
M=D+M //add num2 to sum

@num1
M=M-1 //decrement num1
@ADD
0;JMP //loop to ADD

(DONE)
@mult
D=M
@R2
M=D

(END)
@END
0;JMP
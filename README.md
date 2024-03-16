# Mini-Python compiler - Project report

###### Collaborators:

> Tesnim
> Etienne 

## Mini-Python syntax and semantics
*on peut laisser tomber cette partie*

## Code Generation and Implementation choices

> The code passes all compilation, behavior and output tests.
> 
> A C file containing the custom functions is linked to the generated assembly.
> 
> Compiler written in java using visitors : brief explanation

#### 0. stack alignment
before every function 

#### 1. Register allocation
- follow convention (put the reg table for lecture) for custom C functions
- compiled functions parameters are pushed on the stack directly
- saving and popping registers on the stack 



#### 2. Built-in functions coded in C

- why is it better than battling assembly heads on ... in java!!
- challenges : passing pointers, static vs dynamic allocation of constants / variables
- makefile changes and link ==> need a working pipeline


#### debug strategies:
- continous implementation and debug to solve problems as they pop up
- using valgrind and gdb to anaylyse registers and program behavior in real time
```
gdb layout
gdb regs
```

## optimizations and extensions
- if statement : test AFTER statement body -> reduce CPU cycles
- range function can take 1, 2 or 3 arguments



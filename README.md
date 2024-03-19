# Mini-Python compiler - Project report
use:
```
make                # make minipython script
make clean          # clean the generated files

bash test -2 minipython  # run the typing tests
bash test -3 minipython  # run the code generation tests
```
###### Collaborators:

> Tesnim
> Etienne 

## Code Generation and Implementation choices

- The code passes all compilation, behavior and output tests.

- Compiler written in java using visitors

- A C file containing the custom functions is linked to the generated assembly.
 

#### 1. Stack alignment
We ensured the stack stayed aligned before every function call by manually substracting 16 bytes


#### 2. Register allocation
We can discuss 2 cases: the built-in functions we decided to implement in C *(for reasons discussed in paragraph2)* and the compiled functions from the mini-python file: 
- The compiler adhered to register allocation conventions for custom C functions especially for arguments. We also paid special attention so that our custom functions' arity didn't require pushing more arguments on the stack. Thus passing arguments was usually limited to %rdi, %rsi and sometimes %rdx.
![image](https://hackmd.io/_uploads/BJaCiFV0p.png)

- As for the functions we compiled from the mini-python file, we pushed their arguments directly on the stack and then popped at the end of the function call. Their return values were always in the %rax register. 




#### 3. Built-in functions coded in C

One of the first challenges in coding everything in assembly, including the system calls and the runtime errors, is its complexity. 
Thus we decided to dynamically link the generated code to a custom.c file in which we put built-in functions and other useful utilities. Another advantage this brings is the gcc compiler optimizations' suite. 
The makefile and the test script were promptly updated to include these changes.
Though the pipeline proved somewhat delicate, once in place, it greatly simplified code generation as well as debugging and raising runtime errors.



#### 4. Challenges and Debug strategies:

Challenges were either related to understanding the visitor behavior and how expressions were evaluated, or to static and dynamic memory allocations.
For example a seemingly minor issue that still affected code behavior was how unsigned integers uint64_t were handled in memory. 
Continuous implementation and debugging were employed to address issues promptlyas they appreared. We also used tools like Valgrind and GDB to analyze registers and program behavior in real-time and better tackle memory allocation issues. (INF554)
```
gdb layout asm
gdb layout regs
```

## Optimizations and Extensions
#### 1. FOR Loop optimization

As explained in the lecture, putting the test at the end ensures less branching per iteration.

```
  public void visit(TSfor s) {
    String Loop = genSectionLabel(), Test = genSectionLabel();
    s.e.accept(this); // result in %rax
    x.pushq("%rax");
    x.movq("%rax", "%rdi");
    alignStack(x, () -> {
      x.call("is_list"); // check if it's a list: if not, throw an error
    });
    x.popq("%rax");
    x.leaq("16(%rax)", "%r11");
    x.movq("8(%rax)", "%r12");
    x.leaq("16(%rax,%r12,8)", "%r12");

    x.jmp(Test);

    x.label(Loop);
    x.movq("(%r11)", "%r10");
    x.movq("%r10", s.x.ofs + "(%rbp)");

    saveRegister(x, () -> {
      s.s.accept(this);
    }, new String[] { "%r11", "%r12" });

    x.addq("$8", "%r11");

    x.label(Test);
    x.cmpq("%r11", "%r12");
    x.jne(Loop);

  }
```

#### 2. Implementation of range function
We implemented the range function so that it can take 1, 2 or 3 arguments just like in Python:
- The default values are set if needed in Typing (0 for the first value and 1 for the step)
- All 3 arguments are put in registers just like for normal functions
- Works with negative step: we first calculate the length of the resulting list and then increment the start value by the step



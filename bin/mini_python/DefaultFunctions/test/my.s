
extern custom_function

section .text
global main

main:
    push rbp
    mov rbp, rsp
    and rsp, -16
    call custom_function
    pop rbp

    mov eax, 1        ; syscall number for exit
    xor ebx, ebx      ; status = 0
    int 0x80          ; call kernel

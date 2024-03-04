	.text
	.globl main
main:
	movq $data_0, %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call my_printf
	movq %rbp, %rsp
	popq %rbp
	.data
data_0:
	.quad 3
	.quad 13
	.string "Hello, World!"

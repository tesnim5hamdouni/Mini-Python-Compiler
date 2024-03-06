	.text
	.globl main
main:
	movq $data_0, %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call unop_not
	movq %rbp, %rsp
	popq %rbp
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call my_printf
	movq %rbp, %rsp
	popq %rbp

	.data
data_0:
	.quad 1
	.quad 1

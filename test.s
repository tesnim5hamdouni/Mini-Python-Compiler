	.text
	.globl main
main:
	movq $data_0, %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call unop_neg
	movq %rbp, %rsp
	popq %rbp
	movq $data_1, %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call my_printf
	movq %rbp, %rsp
	popq %rbp

	.data
data_0:
	.quad 2
	.quad 5
data_1:
	.quad 2
	.quad 1

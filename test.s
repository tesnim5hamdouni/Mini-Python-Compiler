	.text
	.globl main
main:
	pushq %rbp
	movq %rsp, %rbp
	subq $16, %rsp
	movq $data_0, %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call unop_neg
	movq %rbp, %rsp
	popq %rbp
	movq %rax, -8(%rbp)
	movq $data_1, %rax
	movq %rax, -16(%rbp)
	movq -8(%rbp), %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call my_printf
	movq %rbp, %rsp
	popq %rbp
	xorq %rax, %rax
	movq %rbp, %rsp
	popq %rbp
	ret

	.data
data_0:
	.quad 2
	.quad 5
data_1:
	.quad 2
	.quad 1

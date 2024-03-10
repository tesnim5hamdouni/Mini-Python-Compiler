	.text
	.globl main
main:
	pushq %rbp
	movq %rsp, %rbp
	subq $8, %rsp
	movq $data_0, %rax
	movq %rax, -8(%rbp)
	movq -8(%rbp), %rax
	cmpq $0, 8(%rax)
	je text_0
	movq $data_1, %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call my_printf
	movq %rbp, %rsp
	popq %rbp
	jmp text_1
text_0:
	movq $data_2, %rax
	movq %rax, %rdi
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call my_printf
	movq %rbp, %rsp
	popq %rbp
text_1:
	xorq %rax, %rax
	movq %rbp, %rsp
	popq %rbp
	ret

	.data
data_0:
	.quad 1
	.quad 1
data_1:
	.quad 3
	.quad 9
	.string "x is True\0"
data_2:
	.quad 3
	.quad 10
	.string "x is False\0"

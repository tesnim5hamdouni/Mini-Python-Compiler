gcc -c /src/mini_python/DefaultFunctions/custom.c -o custom.o
nasm -f elf64 test.s -o test.o
gcc -o program test.o custom.o
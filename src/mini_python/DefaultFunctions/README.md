The simplest way is to implement them in C and call them with extern when building the assembly code.

I noticed that a bad stack alignement can cause a segmentation fault when calling a function from C. This can be fixed by adding a push/pop instruction to align the stack, like seeing in the lecture.

tests to verify the successful linking are in the test folder.
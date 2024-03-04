#include <stdio.h>
#include <stdint.h>

void my_printf(uint64_t * str) {
    //test if 1st field is a string
    if (str[0] == 3) {
        //print char at str[2] of length str[1]
        printf("%s", (char*)(str + 2));
        return;
    }

    // if(str[0] == 1) {
    //     printf("%d\n", (int)str[1]);
    //     return;
    // }
}
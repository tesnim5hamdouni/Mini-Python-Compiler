#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>

void my_printf(uint64_t * str) {
    
    if (str[0] == 0){
        //None value
        printf(" None\n");
        fflush(stdout);

        return;
    }

    if (str[0] == 1){
        //Bool value
        str[1] == 1 ? printf(" True \n") : printf(" False \n");
        fflush(stdout);
        return;
    }

    if (str[0] == 2){
        //Int value
        printf(" %d \n", (int)str[1]);
        fflush(stdout);
        return;
    }

    if (str[0] == 3) {
        //print char at str[2] of length str[1]
        printf("%s", (char*)(str + 2));
        fflush(stdout);
        return;
    }

    if (str[0] == 4){
    // this is a list
        printf("[");
        for (int i = 0; i < str[1]; i++){
            my_printf((uint64_t*)(str + 2 + i));
            if (i != str[1] - 1){
                printf(", ");
            }
        }
        printf("]");
        fflush(stdout);
        return;
    }

}


uint64_t* unop_neg(uint64_t* a){
    if (a[0] != 2){
        printf("Error: unsupported operand type(s) for -\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 2;
    result[1] = -a[1];
    return result;
}

uint64_t *unop_not(uint64_t *a){
    if (a[0] != 1){
        printf("Error: unsupported operand type(s) for not\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 1;
    result[1] = !a[1];
    return result;
}
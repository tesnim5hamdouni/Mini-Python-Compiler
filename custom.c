#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>

void my_printf(uint64_t * str) {
    
    if (str[0] == 0){
        //None value
        printf("None");
        fflush(stdout);

        return;
    }

    if (str[0] == 1){
        //Bool value
        str[1] == 1 ? printf("True") : printf("False");
        fflush(stdout);
        return;
    }

    if (str[0] == 2){
        //Int value
        printf("%d", (int)str[1]);
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
            my_printf((uint64_t*)(*(str + 2 + i)));
            if (i != str[1] - 1){
                printf(", ");
            }
        }
        printf("]");
        fflush(stdout);
        return;
    }

}


void print_newline() {
    printf("\n");
    fflush(stdout);
}

void is_list(uint64_t *a){
    if (a[0] != 4){
        if (a[0] == 0){
            printf("Error: unsupported operand type for type None\n");
        } else if (a[0] == 1){
            printf("Error: unsupported operand type for type bool\n");
        } else if (a[0] == 2){
            printf("Error: unsupported operand type for type int\n");
        } else if (a[0] == 3){
            printf("Error: unsupported operand type for type string\n");
        } else {
            printf("Error: unsupported operand type ");
        }
        fflush(stdout);
        return;
    }


}

void * range(uint64_t * a){
    if (a[0] != 2){
        printf("Error: unsupported argument type for range()\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t * result = (uint64_t *)malloc((a[1] + 2) * sizeof(uint64_t ));
    uint64_t ** temp = (uint64_t **)malloc(a[1] * sizeof(uint64_t *));
    for (int i = 0; i < a[1]; i++){
        temp[i] = (uint64_t *)malloc(2 * sizeof(uint64_t));
        temp[i][0] = 2;
        temp[i][1] = i;
    }
    result[0] = 4;
    result[1] = a[1];
    for (int i = 0; i < a[1]; i++){
        result[2 + i] = (uint64_t)temp[i];
    }

    return result;
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
    // None, false, 0, empty list and empty string are all False --> return True
    // Everything else is True --> return False
    uint64_t *result = (uint64_t *)malloc(2 * sizeof(uint64_t));
    result[0] = 1;
    result[1] = a[0] == 0 || (a[0] == 1 && a[1] == 0) || (a[0] == 2 && a[1] == 0) || (a[0] == 3 && a[1] == 0) || (a[0] == 4 && a[1] == 0);
    return result;
}

uint64_t *binop_add(uint64_t *a, uint64_t *b){
    
    if (a[0] != b[0]){
        printf("Error: cannot add operands of different type\n");
        fflush(stdout);
        return NULL;
    }
    if (a[0] == 0) {
        printf("Error: unsupported operand type None for +\n");
        fflush(stdout);
        return NULL;
    }

    if (a[0] == 1){
        printf("Error: unsupported operand type bool for +\n");
        fflush(stdout);
        return NULL;
    }

    if (a[0] == 2){
        uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
        result[0] = 2;
        result[1] = a[1] + b[1];
        return result;
    }

    if (a[0] == 3){
        uint64_t* result = (uint64_t*)malloc((a[1] + b[1]) * sizeof(char) + 2 * sizeof(uint64_t));
        result[0] = 3;
        result[1] = a[1] + b[1];
        // take the first string
        char* str_a = (char*)(a + 2);
        char* str_b = (char*)(b + 2);
        snprintf((char*)(result + 2), a[1] + b[1] + 1, "%s%s", str_a, str_b);
        return result;        
    }

    if (a[0]==4) {
        uint64_t* result = (uint64_t*)malloc((a[1] + b[1] + 2) * sizeof(uint64_t));
        result[0] = 4;
        result[1] = a[1] + b[1];
        for (int i = 0; i < a[1]; i++){
            result[2 + i] = a[2 + i];
        }
        for (int i = 0; i < b[1]; i++){
            result[2 + a[1] + i] = b[2 + i];
        }
        return result;
    }

}

uint64_t *binop_sub(uint64_t *a, uint64_t *b){
    if (a[0] != 2 || b[0] != 2){
        printf("Error: unsupported operand type(s) for -\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 2;
    result[1] = a[1] - b[1];
    return result;
}

uint64_t *binop_mul(uint64_t *a, uint64_t *b){
    if (a[0] != 2 || b[0] != 2){
        printf("Error: unsupported operand type(s) for *\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 2;
    result[1] = a[1] * b[1];
    return result;
}

uint64_t *binop_div(uint64_t *a, uint64_t *b){
    if (a[0] != 2 || b[0] != 2){
        printf("Error: unsupported operand type(s) for /\n");
        fflush(stdout);
        return NULL;
    }
    if (b[1] == 0){
        printf("Error: division by zero\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 2;
    result[1] = a[1] / b[1];
    return result;
}

uint64_t *binop_mod(uint64_t *a, uint64_t *b){
    if (a[0] != 2 || b[0] != 2){
        printf("Error: unsupported operand type(s) for %%\n");
        fflush(stdout);
        return NULL;
    }
    if (b[1] == 0){
        printf("Error: division by zero\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 2;
    result[1] = a[1] % b[1];
    return result;
}

uint64_t *binop_eq(uint64_t *a, uint64_t *b){
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 1;
    if (a[0] != b[0]){
        result[1] = 0;
    } else if (a[0] == 1 || a[0]==2){ 
        result[1] = a[1] == b[1];
    } else if (a[0] == 3){
        if (a[1] != b[1]){
            result[1] = 0;
        } else {
            result[1] = 1;
            for (int i = 0; i < a[1]; i++){
                if (*((char*)(a + 2 + i)) != *((char*)(b + 2 + i))){
                    result[1] = 0;
                    return result;
                }
            }
            result[1] = 1;
        }
    } else if (a[0] == 4){
        if (a[1] != b[1]){
            result[1] = 0;
        } else {
            for (int i = 0; i < a[1]; i++){
                if (binop_eq((uint64_t*)a[2+i], (uint64_t*)b[2+i])[1] == 0){
                    result[1] = 0;
                    return result;
                }
            }
            result[1] = 1;
        }
    }
    return result;
}

uint64_t *binop_neq(uint64_t *a, uint64_t *b){
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 1;
    result[1] = !binop_eq(a, b)[1];
    return result;
}

uint64_t *binop_lt(uint64_t *a, uint64_t *b){
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 1;
    if (a[0] != b[0]){
        printf("Error: comparison unsupported for different types\n");
        exit(1);
    } else if (a[0] == 1 || a[0] == 2){ 
        result[1] = a[1] < b[1];
    } else if (a[0] == 3){
        int len_min = a[1] < b[1] ? a[1] : b[1];
        for (int i = 0; i < len_min; i++){
            if (*((char*)(a + 2 + i)) < *((char*)(b + 2 + i))){
                result[1] = 1;
                return result;
            } else if (*((char*)(a + 2 + i)) > *((char*)(b + 2 + i))){
                result[1] = 0;
                return result;
            }
        }
        if (a[1] < b[1]){
            result[1] = 1;
        } else {
            result[1] = 0;
        }
    } else if (a[0] == 4){
        int len_min = a[1] < b[1] ? a[1] : b[1];
        for (int i = 0; i < len_min; i++){
            if (binop_lt((uint64_t*)a[2+i], (uint64_t*)b[2+i])[1] == 1){
                result[1] = 1;
                return result;
            } else if (binop_lt((uint64_t*)b[2+i], (uint64_t*)a[2+i])[1] == 1){
                result[1] = 0;
                return result;
            }
        }
        if (a[1] < b[1]){
            result[1] = 1;
        } else {
            result[1] = 0;
        }
    }
    return result;
}

uint64_t *binop_le(uint64_t *a, uint64_t *b){
    uint64_t* result = binop_lt(b, a);
    result[1] = !result[1];
    return result;
}

uint64_t *binop_gt(uint64_t *a, uint64_t *b){
    return binop_lt(b, a);;
}

uint64_t *binop_ge(uint64_t *a, uint64_t *b){
    uint64_t* result = binop_lt(a, b);
    result[1] = !result[1];
    return result;
}

uint64_t *list(int a){
    uint64_t* result = (uint64_t*)malloc((2+a)* sizeof(uint64_t));
    result[0] = 4;
    result[1] = a;
    return result;
}


uint64_t *get(uint64_t *a, uint64_t *b){
    if (a[0] != 4 || b[0] != 2){
        printf("Error: unsupported operand type(s) for []\n");
        fflush(stdout);
        return NULL;
    }
    if (b[1] < 0 || b[1] >= a[1]){
        printf("Error: index out of range\n");
        fflush(stdout);
        return NULL;
    }
    return (uint64_t*)(*(a + 2 + b[1]));
}

uint64_t *set(uint64_t *a, uint64_t *b, uint64_t *c){
    if (a[0] != 4 || b[0] != 2){
        printf("Error: unsupported operand type(s) for []\n");
        fflush(stdout);
        return NULL;
    }
    if (b[1] < 0 || b[1] >= a[1]){
        printf("Error: index out of range\n");
        fflush(stdout);
        return NULL;
    }
    a[2 + b[1]] = (uint64_t) c;
    return a;
}

uint64_t *len(uint64_t *a){
    if (a[0] != 3 && a[0] != 4){
        printf("Error: unsupported operand type for len()\n");
        fflush(stdout);
        return NULL;
    }
    uint64_t* result = (uint64_t*)malloc(2 * sizeof(uint64_t));
    result[0] = 2;
    result[1] = a[1];
    return result;
}




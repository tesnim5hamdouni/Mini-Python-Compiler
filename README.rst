choices made:

    track declared functions: field in TypeChecker --> directly check tfile content in the file functions

    track local vs global variables: 2 fields in TypeChecker depending on whether it's MAIN or not --> add local vars field in Function class

    create a scope context created / updated when a function is called / declared : keep track of recursion but also local variables when handling expressions/statements

    add TElen same as TErange

    

Questions:

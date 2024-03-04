
package mini_python;

public abstract class Type {
    /*
     * Easiest way to handle methods that are type specific
     * 
     */

    public static Type None = new None();
    public static Type Bool;
    public static Type Strng;
    public static Type Int;
    public static Type List;

    public abstract void addToX86Labels(TCompiler v);

    public abstract void my_add(TCompiler v);

    public abstract void my_sub(TCompiler v);

    public abstract void my_mul(TCompiler v);

    public abstract void my_div(TCompiler v);

    public abstract void my_mod(TCompiler v);

    public abstract void my_eq(TCompiler v);

    public abstract void my_neq(TCompiler v);

    public abstract void my_lt(TCompiler v);

    public abstract void my_lte(TCompiler v);

    public abstract void my_gt(TCompiler v);

    public abstract void my_gte(TCompiler v);

    public abstract void my_and(TCompiler v);

    public abstract void my_or(TCompiler v);

    public abstract void my_not(TCompiler v);

    public abstract void my_neg(TCompiler v);

    public abstract void my_int(TCompiler v);

    public abstract void my_len(TCompiler v);

    public abstract void my_print(TCompiler v);

}
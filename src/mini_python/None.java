package mini_python;

import mini_python.ExceptionHandler.IllegalOperation;

public class None extends Type {

    public static final String NONE_NAME = "None";
    public static final String TYPE_ID = "__None__TYPE__";
    public static final String NONE_PRINT = "__None__PRINT__";
    public static final String NONE_LABEL = "$" + TYPE_ID + "LABEL__";

    @Override
    public void addToX86Labels(TCompiler v) {
        v.x.dlabel(NONE_NAME);
        v.x.quadString(TYPE_ID);
        v.x.quad(0);
        v.x.dlabel(NONE_PRINT);
        v.x.string(NONE_NAME);

    }

    @Override
    public void my_add(TCompiler v) {
        throw new IllegalOperation("Illegal Addition on None");
    }

    @Override
    public void my_sub(TCompiler v) {
        throw new IllegalOperation("Illegal Substraction on None");
    }

    @Override
    public void my_mul(TCompiler v) {
        throw new IllegalOperation("Illegal Multiplication on None");
    }

    @Override
    public void my_div(TCompiler v) {
        throw new IllegalOperation("Illegal Division on None");
    }

    @Override
    public void my_mod(TCompiler v) {
        throw new IllegalOperation("Illegal Modulus on None % None");
    }

    @Override
    public void my_eq(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None == None");
    }

    @Override
    public void my_neq(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None != None");
    }

    @Override
    public void my_lt(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None < None");
    }

    @Override
    public void my_lte(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None <= None");
    }

    @Override
    public void my_gt(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None > None");
    }

    @Override
    public void my_gte(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None >= None");
    }

    @Override
    public void my_and(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None && None");
    }

    @Override
    public void my_or(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: None || None");
    }

    @Override
    public void my_not(TCompiler v) {
        v.x.movq("$" + NONE_LABEL, "%rax"); // get bool True Label?
        v.x.ret();
    }

    @Override
    public void my_neg(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: -None");
    }

    @Override
    public void my_int(TCompiler v) {
        v.x.movq(16, "%rdi");
        v.alignStack(v.x, () -> {
            v.x.call("malloc");
            v.x.ret();
        });
        // v.x.movq(Type.Int.TYPE_ID, "(%rax)");
        v.x.movq(0, "8(%rax)");
        v.x.ret();

    }

    @Override
    public void my_len(TCompiler v) {
        throw new IllegalOperation("Illegal Operation: len(None)");
    }

    @Override
    public void my_print(TCompiler v) {
        v.x.movq("$" + NONE_PRINT, "%rdi");
        v.alignStack(v.x, () -> {
            v.x.call("printf");
            v.x.ret();
        });
        v.x.ret();
    }

}

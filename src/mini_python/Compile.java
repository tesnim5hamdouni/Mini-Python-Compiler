package mini_python;

import java.util.HashMap;

class Compile {

  static boolean debug = false;
  static public final String __MAIN__ = "__main__";

  static X86_64 file(TFile f) {
    X86_64 x = new X86_64();
    TCompiler visitor = new TCompiler(x);

    x.globl(__MAIN__);

    // Think about how to handle built-in functions

    for (TDef td : f.l) {
      visitor.visit(td);
    }
    return x;
  }

}

class TCompiler implements TVisitor {

  X86_64 x;
  int dataCount = 0, textCount = 0;

  TCompiler(X86_64 x) {
    this.x = x;
  }

  // create a hash map <T,Label> to keep track of variables, T can be any class

  // HashMap<Object, Lab> varMap = new HashMap<Object, Lab>();

  // Lab manageLabels(Object o) {
  // if (varMap.containsKey(o)) {
  // return varMap.get(o);
  // } else {
  // Lab l = new Lab("v" + varMap.size());
  // varMap.put(o, l);
  // return l;
  // }
  // }

  public X86_64 x86_64Getter() {
    return this.x;
  }

  public String genDataLabel() {
    return "data_" + dataCount++;
  }

  public String genTextLabel() {
    return "text_" + textCount++;
  }

  @Override
  public void visit(Cnone c) {
    x.movq("$__None__", "%rax");
  }

  @Override
  public void visit(Cbool c) {
  }

  @Override
  public void visit(Cstring c) {
    // get a label and add it to data
    String newLabel = genDataLabel();
    x.dlabel(newLabel); // add label to x86_64 data
    x.quad(c.s.length());
    x.string(c.s);
    x.movq("$" + newLabel, "%rax");
  }

  @Override
  public void visit(Cint c) {
  }

  @Override
  public void visit(TEcst e) {
  }

  @Override
  public void visit(TEbinop e) {
  }

  @Override
  public void visit(TEunop e) {
  }

  @Override
  public void visit(TEident e) {
  }

  @Override
  public void visit(TEcall e) {
  }

  @Override
  public void visit(TEget e) {
  }

  @Override
  public void visit(TElist e) {
  }

  @Override
  public void visit(TErange e) {
  }

  @Override
  public void visit(TSif s) {
  }

  @Override
  public void visit(TSreturn s) {
  }

  @Override
  public void visit(TSassign s) {
  }

  @Override
  public void visit(TSprint s) {
    s.accept(this); // visit print's argument --> result in %rax
    x.movq("%rax", "%rdi");
    x.call("print");
    x.movq(null, null)

  }

  @Override
  public void visit(TSblock s) {
  }

  @Override
  public void visit(TSfor s) {
  }

  @Override
  public void visit(TSeval s) {
  }

  @Override
  public void visit(TSset s) {
  }

  @Override
  public void visit(TElen e) {
  }

  @Override
  public void visit(TDef d) {

  }

}

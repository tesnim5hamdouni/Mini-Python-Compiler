package mini_python;

import mini_python.ExceptionHandler.IllegalOperation;
import mini_python.ExceptionHandler.UnknownType;

public class Compile {

  static boolean debug = false;
  static public final String __MAIN__ = "main";

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

  /*
   * Usefule methods to handle registers and stack alignment
   */

  public void saveRegister(X86_64 x, Runnable snippet, String[] regs) {
    int i;
    for (i = 0; i < regs.length; i++) {
      x.pushq(regs[i]);
    }

    snippet.run();

    for (i = regs.length - 1; i >= 0; i--) {
      x.popq(regs[i]);
    }
  }

  public void alignStack(X86_64 x, Runnable snippet) {
    x.pushq("%rbp");
    x.movq("%rsp", "%rbp");
    x.andq("$-16", "%rsp"); // 16-byte stack alignment
    snippet.run();
    x.movq("%rbp", "%rsp");
    x.popq("%rbp");
  }

  /*
   * Handle labels --> data and text : to be optimized later
   */

  public X86_64 x86_64Getter() {
    return this.x;
  }

  // optimize later by using a hash map to keep track of labels
  public String genDataLabel() {
    return "data_" + dataCount++;
  }

  public String genTextLabel() {
    return "text_" + textCount++;
  }

  /*
   * Visit methods
   */

  @Override
  public void visit(Cnone c) {
    String newLabel = genDataLabel();
    x.dlabel(newLabel); // add label to x86_64 data
    x.quad(0);
    x.quad(0);
    x.movq("$" + newLabel, "%rax");
  }

  @Override
  public void visit(Cbool c) {
    System.out.println("Entered Cbool");
    String newLabel = genDataLabel();
    x.dlabel(newLabel); // add label to x86_64 data
    x.quad(1);
    if (c.b)
      x.quad(1);
    else
      x.quad(0);
    x.movq("$" + newLabel, "%rax");
  }

  @Override
  public void visit(Cstring c) {
    // int len = c.s.length() + 1 + 16;

    // alignStack(x, () -> {
    // x.movq("$" + len, "%rdi");
    // x.call("malloc");
    // });
    String newLabel = genDataLabel();
    x.dlabel(newLabel); // add label to x86_64 data
    x.quad(3);
    x.quad(c.s.length());
    x.string(c.s + "\\0");
    x.movq("$" + newLabel, "%rax");

    // saveRegister(x, () -> alignStack(x, () -> {
    // x.movq("%rax", "%rdi");
    // x.movq("$" + newLabel, "%rsx");
    // x.call("strcpy");
    // }), regs);

  }

  @Override
  public void visit(Cint c) {
    String newLabel = genDataLabel();
    x.dlabel(newLabel);
    x.quad(2);
    x.quad(c.i);
    x.movq("$" + newLabel, "%rax");
  }

  @Override
  public void visit(TEcst e) {
    // switch case to handle different types of constants
    if (e.c instanceof Cnone) {
      visit((Cnone) e.c);
    } else if (e.c instanceof Cbool) {
      visit((Cbool) e.c);
    } else if (e.c instanceof Cstring) {
      visit((Cstring) e.c);
    } else if (e.c instanceof Cint) {
      System.out.println("Cint");
      visit((Cint) e.c);
    } else {
      throw new UnknownType("Unknown constant type at location: ");
    }
  }

  @Override
  public void visit(TEbinop e) {
    System.out.println("Entered TEbinop, e.e1 = " + e.e1 + ", e.e2 = " + e.e2);
    e.e1.accept(this); // result in %rax
    x.movq("%rax", "%rdi");
    e.e2.accept(this); // result in %rax
    x.movq("%rax", "%rsi");
    switch (e.op) {
      case Badd:
        System.out.println("Badd");
        alignStack(x, () -> {
          x.call("binop_add");
        });
        break;
      case Bsub:
        System.out.println("Bsub");
        alignStack(x, () -> {
          x.call("binop_sub");
        });
        break;
      case Bmul:
        System.out.println("Bmul");
        alignStack(x, () -> {
          x.call("binop_mul");
        });
        break;
      case Bdiv:
        System.out.println("Bdiv");
        alignStack(x, () -> {
          x.call("binop_div");
        });
        break;
      case Bmod:
        System.out.println("Bmod");
        alignStack(x, () -> {
          x.call("binop_mod");
        });
        break;
      case Band:
        System.out.println("Band");
        alignStack(x, () -> {
          x.call("binop_and");
        });
        break;
      case Bor:
        System.out.println("Bor");
        alignStack(x, () -> {
          x.call("binop_or");
        });
        break;
      case Beq:
        System.out.println("Beq");
        alignStack(x, () -> {
          x.call("binop_eq");
        });
        break;
      case Bneq:
        System.out.println("Bneq");
        alignStack(x, () -> {
          x.call("binop_neq");
        });
        break;
      case Blt:
        System.out.println("Blt");
        alignStack(x, () -> {
          x.call("binop_lt");
        });
        break;
      case Ble:
        System.out.println("Ble");
        alignStack(x, () -> {
          x.call("binop_le");
        });
        break;
      case Bgt:
        System.out.println("Bgt");
        alignStack(x, () -> {
          x.call("binop_gt");
        });
        break;
      case Bge:
        System.out.println("Bge");
        alignStack(x, () -> {
          x.call("binop_ge");
        });
        break;
      default:
        throw new IllegalOperation("Illegal binary operation at location: ");
    }
  }

  @Override
  public void visit(TEunop e) {// need to distinguish neg from not following e.e type
    System.out.println("Entered TEunop, e.e = " + e.e);
    e.e.accept(this); // result in %rax
    switch (e.op) {
      case Uneg:
        System.out.println("Uneg");
        x.movq("%rax", "%rdi");
        alignStack(x, () -> {
          x.call("unop_neg");
        });
        break;
      case Unot:
        System.out.println("Unot");
        x.movq("%rax", "%rdi");
        alignStack(x, () -> {
          x.call("unop_not");
        });
        break;
      default:
        throw new IllegalOperation("Illegal unary operation at location: ");
    }

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
    // String newLabel = genDataLabel();
    // x.dlabel(newLabel); // add label to x86_64 data
    // x.quad(4);
    // x.quad(e.l.size());
    // for (TExpr te : e.l) {
    //   te.accept(this);
    // }
    // x.movq("$" + newLabel, "%rax");
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
    System.out.println("Entered TSprint, s.e = " + s.e);
    s.e.accept(this); // visit print's argument --> result in %rax
    System.out.println("TSprint After accept");
    x.movq("%rax", "%rdi");
    alignStack(x, () -> {
      x.call("my_printf");
    });
  }

  @Override
  public void visit(TSblock s) {
    for (TStmt ts : s.l) {
      ts.accept(this);
    }
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
    String newLabel = d.f.name;
    x.label(newLabel);
    d.body.accept(this);
  }

}

package mini_python;

import java.util.HashMap;

import mini_python.ExceptionHandler.IllegalOperation;
import mini_python.ExceptionHandler.UnknownType;
import mini_python.ExceptionHandler.CompilationError;

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

  // create a hash map to store vaiables and their labels
  // HashMap<Variable, Lab> varMap = new HashMap<Variable, Lab>();

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

    String newLabel = genDataLabel();
    x.dlabel(newLabel); // add label to x86_64 data
    x.quad(3);
    x.quad(c.s.length());
    x.string(c.s + "\\0");
    x.movq("$" + newLabel, "%rax");

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
      System.out.println("Cstring");
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
    x.pushq("%rax");
    e.e2.accept(this); // result in %rax
    x.popq("%rdi"); // put result of e.e1 in %rdi
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
    System.out.println("Entered TEident, e.x = " + e.x.name);
    if (e.x.ofs == -1) {
      throw new CompilationError("Variable " + e.x.name + " not found");
    }
    x.movq(e.x.ofs + "(%rbp)", "%rax");
  }

  @Override
  public void visit(TEcall e) {
    System.out.println("Entered TEcall, e.f = " + e.f.name);
    // following the stack layout scheme suggested
    // we'll push the arguments in reverse order
    for (int i = e.l.size() - 1; i >= 0; i--) {
      e.l.get(i).accept(this); // result in %rax
      x.pushq("%rax");
    }
    x.call(e.f.name);

  }

  @Override
  public void visit(TEget e) {
    System.out.println("Entered TEget, e.e1 = " + e.e1 + ", e.e2 = " + e.e2);
    e.e1.accept(this); // result in %rax
    x.pushq("%rax");
    e.e2.accept(this); // result in %rax
    x.popq("%rdi");
    x.movq("%rax", "%rsi");
    alignStack(x, () -> {
      x.call("get");
    });

  }

  @Override
  public void visit(TElist e) {
    // create empty list in memory using custom function, then set its elements using set

    System.out.println("Entered TElist, e.l = " + e.l);
    x.movq("$" + e.l.size(), "%rdi");
    alignStack(x, () -> {
      x.call("list"); // result in %rax
    });
    x.movq("%rax", "%rdi");
    for(int i = 0; i < e.l.size(); i++){
      x.pushq("%rdi");
      TExpr te = e.l.get(i);
      te.accept(this); // result in %rax
      x.popq("%rdi");
      x.movq("%rax", (2+i)*8 + "(%rdi)");
    }
    x.movq("%rdi", "%rax");
  }

  @Override
  public void visit(TErange e) {
    e.e.accept(this); // result in %rax
    x.movq("%rax", "%rdi");
    alignStack(x, () -> {
      x.call("range"); // TODO: implement range function in custom.c
    });
  }

  @Override
  public void visit(TSif s) {
    System.out.println("Entered TSif, s.s1 = " + s.s1 + " s.s2 = " + s.s2);
    String elseLabel = genTextLabel(), endLabel = genTextLabel(); // generate labels for both jumps
    s.e.accept(this); // &bool in %rax
    x.cmpq("$0", "8(%rax)"); // bool value is in 2nd field
    x.je(elseLabel);
    s.s1.accept(this);
    x.jmp(endLabel);
    x.label(elseLabel);
    s.s2.accept(this);
    x.label(endLabel);
  }

  @Override
  public void visit(TSreturn s) {
    System.out.println("Entered TSreturn, s.e = " + s.e);
    s.e.accept(this); // result in %rax
    x.movq("%rbp", "%rsp");
    x.popq("%rbp");
    x.ret();
  }

  @Override
  public void visit(TSassign s) {
    s.e.accept(this); // result in %rax
    System.out.println("TSassign, s.x = " + s.x.name + " offest = " + s.x.ofs);
    x.movq("%rax", s.x.ofs + "(%rbp)");

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
    s.e.accept(this);
  }

  @Override
  public void visit(TSset s) {
    System.out.println("Entered TSset, s.e1 = " + s.e1 + ", s.e2 = " + s.e2 + ", s.e3 = " + s.e3);
    s.e1.accept(this); // result in %rax
    x.movq("%rax", "%rdi");
    s.e2.accept(this); // result in %rax
    x.movq("%rax", "%rsi");
    s.e3.accept(this); // result in %rax
    x.movq("%rax", "%rdx");
    alignStack(x, () -> {
      x.call("set");
    });
  }

  @Override
  public void visit(TElen e) {
    e.e.accept(this); // result in %rax
    x.movq("%rax", "%rdi");
    alignStack(x, () -> {
      x.call("len"); // TODO: implement len function in custom.c
    });
  }

  @Override
  public void visit(TDef d) {
    int index;
    x.label(d.f.name);
    System.out.println("Entered TDef, d.f.name = " + d.f.name);

    x.pushq("%rbp");
    x.movq("%rsp", "%rbp");

    // make space for local vars if necessary
    if (!d.f.localByName.isEmpty()) {
      x.subq("$" + (d.f.localByName.size() * 8), "%rsp");
      index = 0;
      // set offset for every var in the local list, which is a hash map
      for (HashMap.Entry<String, Variable> entry : d.f.localByName.entrySet()) {
        entry.getValue().ofs = -8 - 8 * index++;
        System.out.println("Local var: " + entry.getValue().name + " offset: " + entry.getValue().ofs);
      }
      // update these offsets in d.f.local
      for (HashMap.Entry<Ident, Variable> entry : d.f.local.entrySet()) {
        entry.getValue().ofs = d.f.localByName.get(entry.getValue().name).ofs;
      }
    }

    d.body.accept(this);

    // cleanup following whether it's main or not
    if (d.f.name.equals(Compile.__MAIN__)) {
      x.xorq("%rax", "%rax");
      x.movq("%rbp", "%rsp");
      x.popq("%rbp");
      x.ret();
    } else {
      visit(new TSreturn(new TEcst(new Cnone())));
    }
  }

}

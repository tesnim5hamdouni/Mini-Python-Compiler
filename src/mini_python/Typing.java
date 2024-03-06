package mini_python;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

class Typing {

  static boolean debug = false;
  public static List<String> PRE_DEF_FUNC = Arrays.asList("range", "len", "list");
  public static String MAIN = "main";

  // use this method to signal typing errors
  static void error(Location loc, String msg) {
    throw new Error(loc + "\nerror: " + msg);
  }

  static TFile file(File f) {

    TFile tf = new TFile();
    Function mainF = new Function(MAIN, new LinkedList<Variable>());
    TypeChecker typeChecker = new TypeChecker(mainF);
    

    for (Def def : f.l) {
      // create the Function object for def
      Function func = new Function(def.f.id, new LinkedList<Variable>());

      // check if function is already declared in tf.l
      for (TDef fun : tf.l)
        if (fun.f.name.equals(func.name))
          Typing.error(def.f.loc, "duplicate function definition '" + func.name + "'");

      // add params and check for duplicates:
      for (Ident paramIdent : def.l) {
        for (Variable var : func.params)
          if (var.name.equals(paramIdent.id))
            Typing.error(paramIdent.loc, "duplicate function argument identifier '" + paramIdent.id + "'");
        func.params.add(Variable.mkVariable(paramIdent.id));
      }
      typeChecker.funStack.push(func);
      typeChecker.setContextF(func); // set context to function being defined
      def.s.accept(typeChecker);

      TStmt body = typeChecker.evaluate(TStmt.class);
      TDef tDef = new TDef(func, body);
      tf.l.add(tDef);
      typeChecker.funStack.pop();
    }

    typeChecker.setContextF(mainF); // after all the fonction definitions, set context to main
    f.s.accept(typeChecker);

    TStmt body = typeChecker.evaluate(TStmt.class);
    TDef tDef = new TDef(mainF, body);
    tf.l.add(tDef);

    return tf;
  }

}

class TypeChecker implements Visitor {

  Object value = null;
  Function contextF; // defines current function scope
  Function mainF; // main function defined from file statement body
  Stack<Function> funStack = new Stack<Function>(); // keep track of the current function scope
  

  // keep track of user declared functions
  HashMap<String, Function> functions = new HashMap<>();

  TypeChecker(Function mainF) {
    this.mainF = mainF;
    funStack.push(mainF);
  }

  public void setContextF(Function f) {
    if (!functions.containsKey(f.name)) {
      functions.put(f.name, f);
    }
    // if previous contextF is MAIN, save local variables
    if (contextF != null && contextF.name.equals(Typing.MAIN)) {
      mainF.local = contextF.local;
    }
    contextF = f;
  }

  public <T> T evaluate(Class<T> retType) {
    assert this.value == null; // check for reentrance
    Object v = this.value;
    this.value = null;
    return (T) v;
  }

  private TExpr evalExpr(Expr e) {
    e.accept(this);
    return evaluate(TExpr.class);
  }

  private TStmt evalStmt(Stmt s) {
    s.accept(this);
    return evaluate(TStmt.class);
  }

  private Variable manageVariable(Ident id, boolean isAssign) {
    // first check if it's a global variable
    Variable v = mainF.local.get(id.id);
    if (v != null) {
      return v;
    }
    // first check if the variable is already declared as params:
    for (Variable var : contextF.params) {
      if (var.name.equals(id.id)) {
        return var;
      }
    }
    // then check if the variable is already declared as local variable:
    v = contextF.local.get(id.id);
    if (v != null) {
      return v;
    }

    if (isAssign) {
      // if not, create a new variable and add it to the local scope
      v = Variable.mkVariable(id.id);
      contextF.local.put(id.id, v);
      // BEWARE here we're updating the contextF object,
      // so when before calling another function, we should same the context in mainF
      return v;
    } 
    else {
      Typing.error(id.loc, "variable " + id.id + " is not declared");
      return null;
    }
  }

  private Function manageFunctionCalls(Ident f) {
    // check for recursive call
    if (!contextF.name.isEmpty() && contextF.name.equals(f.id)) {
      return contextF;
    }
    // check if function is already declared
    Function func = functions.get(f.id);
    if (func != null) {
      return func;
    }
    // else:
    Typing.error(f.loc, "function " + f.id + " is not declared");
    return null;
  }

  @Override
  public void visit(Cnone c) {
    value = new TEcst(c);
  }

  @Override
  public void visit(Cbool c) {
    value = new TEcst(c);
  }

  @Override
  public void visit(Cstring c) {
    value = new TEcst(c);
  }

  @Override
  public void visit(Cint c) {
    value = new TEcst(c);
  }

  @Override
  public void visit(Ecst e) {
    value = new TEcst(e.c);
  }

  @Override
  public void visit(Ebinop e) {
    value = new TEbinop(e.op, evalExpr(e.e1), evalExpr(e.e2));
  }

  @Override
  public void visit(Eunop e) {
    value = new TEunop(e.op, evalExpr(e.e));
  }

  @Override
  public void visit(Eident e) {
    Variable v = manageVariable(e.x, false);
    value = new TEident(v);
  }

  @Override // still
  public void visit(Ecall e) {

    switch (e.f.id) {
      case "range":
        Typing.error(e.f.loc, "range function is not allowed here");
        break;

      case "len":
        if (e.l.size() != 1) {
          Typing.error(e.f.loc, "len function takes 1 argument");
        }
        value = new TElen(evalExpr(e.l.get(0)));
        break;

      case "list":
        if (e.l.size() != 1)
          Typing.error(e.f.loc, "list function takes 1 argument");
        if (!(e.l.get(0) instanceof Ecall))
          Typing.error(e.f.loc, "list function takes a function call as argument");
        Ecall ecall = (Ecall) e.l.get(0);
        if (!ecall.f.id.equals("range"))
          Typing.error(e.f.loc, "list function takes range as argument");
        if (ecall.l.size() != 1)
          Typing.error(e.f.loc, "range function takes 1 argument");
        value = new TErange(evalExpr(ecall.l.get(0)));
        break;

      default:
        Function f = manageFunctionCalls(e.f);
        // check arity
        if (f.params.size() != e.l.size()) {
          Typing.error(e.f.loc, "function " + f.name + " takes " + f.params.size() + " arguments, got " + e.l.size());
        }
        LinkedList<TExpr> tList = new LinkedList<>();
        for (Expr expr : e.l) {
          tList.add(evalExpr(expr));
        }
        // set new context:
        funStack.push(f);
        setContextF(f);
        value = new TEcall(f, tList);
        funStack.pop();
        setContextF(funStack.peek());

    }

  }

  @Override
  public void visit(Eget e) {
    value = new TEget(evalExpr(e.e1), evalExpr(e.e2));
  }

  @Override
  public void visit(Elist e) {
    LinkedList<TExpr> tList = new LinkedList<>();
    for (Expr expr : e.l) {
      tList.add(evalExpr(expr));
    }
    value = new TElist(tList);
  }

  @Override
  public void visit(Sif s) {
    value = new TSif(evalExpr(s.e), evalStmt(s.s1), evalStmt(s.s2));
  }

  @Override
  public void visit(Sreturn s) {
    value = new TSreturn(evalExpr(s.e));
  }

  @Override
  public void visit(Sassign s) {
    value = new TSassign(manageVariable(s.x, true), evalExpr(s.e));
  }

  @Override
  public void visit(Sprint s) {
    value = new TSprint(evalExpr(s.e));
  }

  @Override
  public void visit(Sblock s) {
    LinkedList<TStmt> tStmts = new LinkedList<TStmt>();
    for (Stmt st : s.l) {
      tStmts.add(evalStmt(st));
    }
    value = new TSblock(tStmts);
  }

  @Override
  public void visit(Sfor s) {
    value = new TSfor(manageVariable(s.x, true), evalExpr(s.e), evalStmt(s.s));
  }

  @Override
  public void visit(Seval s) {
    value = new TSeval(evalExpr(s.e));
  }

  @Override
  public void visit(Sset s) {
    value = new TSset(evalExpr(s.e1), evalExpr(s.e2), evalExpr(s.e3));
  }

}

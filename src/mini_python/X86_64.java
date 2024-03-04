package mini_python;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

/** une étiquette (Lab) ou une instruction (Asm) */
abstract class LabelAsm {
  String s;
}

class Lab extends LabelAsm {
  Lab(String s) {
    this.s = s;
  }
}

class Asm extends LabelAsm {
  Asm(String s) {
    this.s = s;
  }
}

/** programme assembleur x86-64 */
public class X86_64 {
  /** segment de code */
  private LinkedList<LabelAsm> text;
  private StringBuffer inline;
  /** segment de données */
  private StringBuffer data;

  X86_64() {
    this.text = new LinkedList<>();
    this.inline = new StringBuffer();
    this.data = new StringBuffer();
  }

  /** ajoute une nouvelle instruction à la fin du code */
  public X86_64 emit(String s) {
    this.text.add(new Asm("\t" + s + "\n"));
    return this;
  }

  /**
   * ajoute une étiquette (par ex. l'étiquette
   * d'une fonction)
   */
  public X86_64 label(String s) {
    this.text.add(new Lab(s));
    return this;
  }

  /**
   * ajoute de l'assembleur à la fin de la zone text
   * (par exemple pour ajouter des pirmitives écrites en assembleur)
   */
  public X86_64 inline(String s) {
    this.inline.append(s);
    return this;
  }

  public X86_64 movq(String op1, String op2) {
    return emit("movq " + op1 + ", " + op2);
  }

  public X86_64 movq(int n, String op) {
    return movq("$" + n, op);
  }

  public X86_64 movzbq(String op1, String op2) {
    return emit("movzbq " + op1 + ", " + op2);
  }

  public X86_64 incq(String op) {
    return emit("incq " + op);
  }

  public X86_64 decq(String op) {
    return emit("decq " + op);
  }

  public X86_64 negq(String op) {
    return emit("negq " + op);
  }

  public X86_64 addq(String op1, String op2) {
    return emit("addq " + op1 + ", " + op2);
  }

  public X86_64 subq(String op1, String op2) {
    return emit("subq " + op1 + ", " + op2);
  }

  public X86_64 imulq(String op1, String op2) {
    return emit("imulq " + op1 + ", " + op2);
  }

  public X86_64 idivq(String op) {
    return emit("idivq " + op);
  }

  public X86_64 cqto() {
    return emit("cqto");
  }

  public X86_64 leaq(String op1, String op2) {
    return emit("leaq " + op1 + ", " + op2);
  }

  public X86_64 notq(String op) {
    return emit("notq " + op);
  }

  public X86_64 andq(String op1, String op2) {
    return emit("andq " + op1 + ", " + op2);
  }

  public X86_64 orq(String op1, String op2) {
    return emit("orq " + op1 + ", " + op2);
  }

  public X86_64 xorq(String op1, String op2) {
    return emit("xorq " + op1 + ", " + op2);
  }

  public X86_64 shlq(String op1, String op2) {
    return emit("shlq " + op1 + ", " + op2);
  }

  public X86_64 shrq(String op1, String op2) {
    return emit("shrq " + op1 + ", " + op2);
  }

  public X86_64 sarq(String op1, String op2) {
    return emit("sarq " + op1 + ", " + op2);
  }

  public X86_64 pushq(String op) {
    return emit("pushq " + op);
  }

  public X86_64 popq(String op) {
    return emit("popq " + op);
  }

  public X86_64 ret() {
    return emit("ret");
  }

  public X86_64 leave() {
    return emit("leave");
  }

  public X86_64 call(String s) {
    return emit("call " + s);
  }

  public X86_64 callstar(String op) {
    return emit("call *" + op);
  }

  public X86_64 jmp(String s) {
    return emit("jmp " + s);
  }

  public X86_64 jmpstar(String op) {
    return emit("jmp *" + op);
  }

  public X86_64 cmpb(String op1, String op2) {
    return emit("cmpb " + op1 + ", " + op2);
  }

  public X86_64 cmpb(int n, String op) {
    return cmpb("$" + n, op);
  }

  public X86_64 cmpw(String op1, String op2) {
    return emit("cmpw " + op1 + ", " + op2);
  }

  public X86_64 cmpw(int n, String op) {
    return cmpw("$" + n, op);
  }

  public X86_64 cmpl(String op1, String op2) {
    return emit("cmpl " + op1 + ", " + op2);
  }

  public X86_64 cmpl(int n, String op) {
    return cmpl("$" + n, op);
  }

  public X86_64 cmpq(String op1, String op2) {
    return emit("cmpq " + op1 + ", " + op2);
  }

  public X86_64 cmpq(int n, String op) {
    return cmpq("$" + n, op);
  }

  public X86_64 testq(String op1, String op2) {
    return emit("testq " + op1 + ", " + op2);
  }

  public X86_64 testq(int n, String op) {
    return testq("$" + n, op);
  }

  public X86_64 je(String s) {
    return emit("je " + s);
  }

  public X86_64 jz(String s) {
    return emit("jz " + s);
  }

  public X86_64 jne(String s) {
    return emit("jne " + s);
  }

  public X86_64 jnz(String s) {
    return emit("jnz " + s);
  }

  public X86_64 js(String s) {
    return emit("js " + s);
  }

  public X86_64 jns(String s) {
    return emit("jns " + s);
  }

  public X86_64 jg(String s) {
    return emit("jg " + s);
  }

  public X86_64 jge(String s) {
    return emit("jge " + s);
  }

  public X86_64 jl(String s) {
    return emit("jl " + s);
  }

  public X86_64 jle(String s) {
    return emit("jle " + s);
  }

  public X86_64 ja(String s) {
    return emit("ja " + s);
  }

  public X86_64 jae(String s) {
    return emit("jae " + s);
  }

  public X86_64 jb(String s) {
    return emit("jb " + s);
  }

  public X86_64 jbe(String s) {
    return emit("jbe " + s);
  }

  public X86_64 sete(String s) {
    return emit("sete " + s);
  }

  public X86_64 setz(String s) {
    return emit("setz " + s);
  }

  public X86_64 setne(String s) {
    return emit("setne " + s);
  }

  public X86_64 setnz(String s) {
    return emit("setnz " + s);
  }

  public X86_64 sets(String s) {
    return emit("sets " + s);
  }

  public X86_64 setns(String s) {
    return emit("setns " + s);
  }

  public X86_64 setg(String s) {
    return emit("setg " + s);
  }

  public X86_64 setge(String s) {
    return emit("setge " + s);
  }

  public X86_64 setl(String s) {
    return emit("setl " + s);
  }

  public X86_64 setle(String s) {
    return emit("setle " + s);
  }

  public X86_64 seta(String s) {
    return emit("seta " + s);
  }

  public X86_64 setae(String s) {
    return emit("setae " + s);
  }

  public X86_64 setb(String s) {
    return emit("setb " + s);
  }

  public X86_64 setbe(String s) {
    return emit("setbe " + s);
  }

  /** segment de données */

  private static String escaped(String s) {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '\n')
        b.append("\\n");
      else
        b.append(c);
    }
    return b.toString();
  }

  /** ajoute une étiquette dans le segment de données */
  public X86_64 dlabel(String s) {
    this.data.append(s + ":\n");
    return this;
  }

  public X86_64 data(String s) {
    this.data.append("\t" + s + "\n");
    return this;
  }

  public X86_64 string(String s) {
    return data(".string \"" + escaped(s) + "\"");
  }

  public X86_64 space(int n) {
    return data(".space " + n);
  }

  public X86_64 quad(long l) {
    return data(".quad " + l);
  }

  public X86_64 quadString(String l) {
    return data(".quad " + l);
  }

  public X86_64 globl(String l) {
    return emit(".globl " + l);
  }

  /** imprime le programme assembleur dans un fichier */
  void printToFile(String file) {
    try {
      Writer writer = new FileWriter(file);
      writer.write("\t.text\n");
      for (LabelAsm lasm : this.text) {
        if (lasm instanceof Lab) {
          writer.write(lasm.s + ":\n");
        } else
          writer.write(lasm.s);
      }
      writer.write(this.inline.toString());
      writer.write("\t.data\n");
      writer.write(this.data.toString());
      writer.close();
    } catch (IOException e) {
      throw new Error("cannot write to " + file);
    }
  }

}

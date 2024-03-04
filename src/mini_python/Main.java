package mini_python;

public class Main {

  static boolean parse_only = false;
  static boolean type_only = false;
  static boolean debug = false;
  static String file = null;

  static void usage() {
    System.err.println("minipython [--parse-only] [--type-only] file.py");
    System.exit(1);
  }

  public static void main(String[] args) throws Exception {
    for (String arg : args)
      if (arg.equals("--parse-only"))
        parse_only = true;
      else if (arg.equals("--type-only"))
        type_only = true;
      else if (arg.equals("--debug")) {
        debug = true;
        Typing.debug = true;
        Compile.debug = true;
      } else {
        if (file != null)
          usage();
        if (!arg.endsWith(".py"))
          usage();
        file = arg;
      }
    if (file == null)
      file = "test.py";

    java.io.Reader reader = new java.io.FileReader(file);
    Lexer lexer = new MyLexer(reader);
    MyParser parser = new MyParser(lexer);
    try {
      File f = (File) parser.parse().value;
      if (parse_only)
        System.exit(0);
      TFile tf = Typing.file(f);
      if (type_only)
        System.exit(0);
      X86_64 asm = Compile.file(tf);
      String file_s = file.substring(0, file.length() - 2) + "s";
      asm.printToFile(file_s);
    } catch (Exception e) {
      System.out.println(file + ":" + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (Error e) {
      System.out.println(file + ":" + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

}

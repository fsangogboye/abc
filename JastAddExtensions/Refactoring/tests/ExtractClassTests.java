package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import AST.ClassDecl;
import AST.FieldDeclaration;
import AST.Program;
import AST.RawCU;
import AST.RefactoringException;
import AST.TypeDecl;

public class ExtractClassTests extends TestCase {
	public ExtractClassTests(String name) {
		super(name);
	}
	
	public void testSucc(String newClassName, String newFieldName, String[] fns, Program in, Program out) {
		assertNotNull(in);
		assertNotNull(out);
		TypeDecl td = in.findType("p", "A");
		assertTrue(td instanceof ClassDecl);
		ArrayList<FieldDeclaration> fds = new ArrayList<FieldDeclaration>();
		for(String fn : fns) {
			FieldDeclaration fd = td.findField(fn);
			assertNotNull(fd);
			fds.add(fd);
		}
		try {
			((ClassDecl)td).doExtractClass(fds, newClassName, newFieldName, true, false);
			assertEquals(out.toString(), in.toString());
		} catch(RefactoringException rfe) {
			assertEquals(out.toString(), rfe.getMessage());
		}
	}
	
	public void testFail(String newClassName, String newFieldName, String[] fns, Program in) {
		assertNotNull(in);
		TypeDecl td = in.findType("p", "A");
		assertTrue(td instanceof ClassDecl);
		ArrayList<FieldDeclaration> fds = new ArrayList<FieldDeclaration>();
		for(String fn : fns) {
			FieldDeclaration fd = td.findField(fn);
			assertNotNull(fd);
			fds.add(fd);
		}
		try {
			((ClassDecl)td).doExtractClass(fds, newClassName, newFieldName, true, false);
			assertEquals("<failure>", in.toString());
		} catch(RefactoringException rfe) {
		}
	}
	
    public void test1() {
        testSucc("Data", "data", new String[] { "x", "y" },
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "class B {" +
            "	int data = 0;" +
            "}" +
            "" +
            "public class A extends B {" +
            "	" +
            "	int x = init();" +
            "	Data y = new Data();" +
            "	" +
            "	public void f() {" +
            "		int data;" +
            "		x = 0;" +
            "		y = new Data();" +
            "		this.x = 2;" +
            "		this.y.z = 3;" +
            "	}" +
            "" +
            "	public int init() {" +
            "		return 4 + data;" +
            "	}" +
            "	" +
            "	public void g() {" +
            "		Data data;" +
            "	}" +
            "	" +
            "}" +
            "" +
            "class Data {" +
            "	int z;" +
            "}" +
            "")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "class B {" +
            "  int data = 0;" +
            "  B() {" +
            "    super();" +
            "  }" +
            "}" +
            "" +
            "public class A extends B {" +
            "  public void f() {" +
            "    int data;" +
            "    this.data.setX(0);" +
            "    this.data.setY(new p.Data());" +
            "    this.data.setX(2);" +
            "    this.data.getY().z = 3;" +
            "  }" +
            "  public int init() {" +
            "    return 4 + super.data;" +
            "  }" +
            "  public void g() {" +
            "    p.Data data;" +
            "  }" +
            "  public A() {" +
            "    super();" +
            "  }" +
            "  " +
            "  static class Data {" +
            "    Data(int x, p.Data y) {" +
            "      super();" +
            "      this.setX(x);" +
            "      this.setY(y);" +
            "    }" +
            "    private int x;" +
            "    private p.Data y;" +
            "    int getX() {" +
            "      return x;" +
            "    }" +
            "    int setX(int x) {" +
            "      return this.x = x;" +
            "    }" +
            "    p.Data getY() {" +
            "      return y;" +
            "    }" +
            "    p.Data setY(p.Data y) {" +
            "      return this.y = y;" +
            "    }" +
            "  }" +
            "  Data data = new Data(init(), new p.Data());" +
            "}" +
            "" +
            "class Data {" +
            "  int z;" +
            "  Data() {" +
            "    super();" +
            "  }" +
            "}")));
    }

    public void test2() {
        testSucc("Data", "d", new String[] { "x" },
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "class B {" +
            "	int d = 0;" +
            "}" +
            "" +
            "public class A extends B {" +
            "	" +
            "	int x = init();" +
            "	" +
            "	public int init() {" +
            "		return 4 + d;" +
            "	}" +
            "	" +
            "}" +
            "")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "class B {" +
            "  int d = 0;" +
            "  B() {" +
            "    super();" +
            "  }" +
            "}" +
            "" +
            "public class A extends B {" +
            "  public int init() {" +
            "    return 4 + super.d;" +
            "  }" +
            "  public A() {" +
            "    super();" +
            "  }" +
            "  " +
            "  static class Data {" +
            "    Data(int x) {" +
            "      super();" +
            "      this.setX(x);" +
            "    }" +
            "    private int x;" +
            "    int getX() {" +
            "      return x;" +
            "    }" +
            "    int setX(int x) {" +
            "      return this.x = x;" +
            "    }" +
            "  }" +
            "  Data d = new Data(init());" +
            "}")));
    }
    
    public void test3() {
        testSucc("Data", "data", new String[] { "x" },
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "public class A {" +
            "	Data x;" +
            "}" +
            "" +
            "class Data {" +
            "" +
            "}")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "public class A {" +
            "  public A() {" +
            "    super();" +
            "  }" +
            "  " +
            "  static class Data {" +
            "    Data() {" +
            "      super();" +
            "    }" +
            "    private p.Data x;" +
            "    p.Data getX() {" +
            "      return x;" +
            "    }" +
            "    p.Data setX(p.Data x) {" +
            "      return this.x = x;" +
            "    }" +
            "  }" +
            "  Data data = new Data();" +
            "}" +
            "" +
            "class Data {" +
            "  Data() {" +
            "    super();" +
            "  }" +
            "}")));
    }

    public void test4() {
        testSucc("Data", "java", new String[] { "x", "y" },
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "class A {" +
            "    int x;" +
            "    int y;" +
            "    { java.lang.System.out.println(); }" +
            "}" +
            "")),
            Program.fromCompilationUnits(new RawCU("A.java",
            "package p;" +
            "" +
            "class A {" +
            "  {" +
            "    System.out.println();" +
            "  }" +
            "  A() {" +
            "    super();" +
            "  }" +
            "  " +
            "  static class Data {" +
            "    Data() {" +
            "      super();" +
            "    }" +
            "    private int x;" +
            "    private int y;" +
            "    int getX() {" +
            "      return x;" +
            "    }" +
            "    int setX(int x) {" +
            "      return this.x = x;" +
            "    }" +
            "    int getY() {" +
            "      return y;" +
            "    }" +
            "    int setY(int y) {" +
            "      return this.y = y;" +
            "    }" +
            "  }" +
            "  Data java = new Data();" +
            "}")));
    }
    
    public void test5() {
    	testFail("Data", "data", new String[]{"x", "y"},
    		Program.fromCompilationUnits(new RawCU("A.java",
    		"package p;" +
    		"class Super {" +
    		"  int f() { return 23; }" +
    		"}" +
    		"" +
    		"class A extends Super {" +
    		"  int x = f();" +
    		"  int y = x + 19;" +
    		"}")));
    }

}
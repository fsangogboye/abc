
package AST;
import java.util.HashSet;import java.util.LinkedHashSet;import java.io.FileNotFoundException;import java.io.File;import java.util.*;import beaver.*;import java.util.ArrayList;import java.util.zip.*;import java.io.*;import sun.text.normalizer.UTF16;import changes.*;import main.FileRange;
public class Program extends ASTNode implements Cloneable {
    public void flushCache() {
        super.flushCache();
        typeObject_computed = false;
        typeObject_value = null;
        typeCloneable_computed = false;
        typeCloneable_value = null;
        typeSerializable_computed = false;
        typeSerializable_value = null;
        typeBoolean_computed = false;
        typeBoolean_value = null;
        typeByte_computed = false;
        typeByte_value = null;
        typeShort_computed = false;
        typeShort_value = null;
        typeChar_computed = false;
        typeChar_value = null;
        typeInt_computed = false;
        typeInt_value = null;
        typeLong_computed = false;
        typeLong_value = null;
        typeFloat_computed = false;
        typeFloat_value = null;
        typeDouble_computed = false;
        typeDouble_value = null;
        typeString_computed = false;
        typeString_value = null;
        typeVoid_computed = false;
        typeVoid_value = null;
        typeNull_computed = false;
        typeNull_value = null;
        unknownType_computed = false;
        unknownType_value = null;
        hasPackage_String_values = null;
        lookupType_String_String_values = null;
        unknownConstructor_computed = false;
        unknownConstructor_value = null;
        getPackageDecl_String_values = null;
    collect_contributors_TypeDecl_uses = false;
    collect_contributors_FieldDeclaration_uses = false;
    collect_contributors_VariableDeclaration_uses = false;
    collect_contributors_ParameterDeclaration_uses = false;
    collect_contributors_MethodDecl_definiteUses = false;
    collect_contributors_MethodDecl_uses = false;
    collect_contributors_MethodDecl_overriders = false;
    collect_contributors_Stmt_pred = false;
    collect_contributors_PackageDecl_prefixUses = false;
    }
    public Object clone() throws CloneNotSupportedException {
        Program node = (Program)super.clone();
        node.typeObject_computed = false;
        node.typeObject_value = null;
        node.typeCloneable_computed = false;
        node.typeCloneable_value = null;
        node.typeSerializable_computed = false;
        node.typeSerializable_value = null;
        node.typeBoolean_computed = false;
        node.typeBoolean_value = null;
        node.typeByte_computed = false;
        node.typeByte_value = null;
        node.typeShort_computed = false;
        node.typeShort_value = null;
        node.typeChar_computed = false;
        node.typeChar_value = null;
        node.typeInt_computed = false;
        node.typeInt_value = null;
        node.typeLong_computed = false;
        node.typeLong_value = null;
        node.typeFloat_computed = false;
        node.typeFloat_value = null;
        node.typeDouble_computed = false;
        node.typeDouble_value = null;
        node.typeString_computed = false;
        node.typeString_value = null;
        node.typeVoid_computed = false;
        node.typeVoid_value = null;
        node.typeNull_computed = false;
        node.typeNull_value = null;
        node.unknownType_computed = false;
        node.unknownType_value = null;
        node.hasPackage_String_values = null;
        node.lookupType_String_String_values = null;
        node.unknownConstructor_computed = false;
        node.unknownConstructor_value = null;
        node.getPackageDecl_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
    return node;
    }
    public ASTNode copy() {
      try {
          Program node = (Program)clone();
          if(children != null) node.children = (ASTNode[])children.clone();
          return node;
      } catch (CloneNotSupportedException e) {
      }
      System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
      return null;
    }
    public ASTNode fullCopy() {
        Program res = (Program)copy();
        for(int i = 0; i < getNumChild(); i++) {
          ASTNode node = getChildNoTransform(i);
          if(node != null) node = node.fullCopy();
          res.setChild(node, i);
        }
        return res;
    }
    // Declared in ClassPath.jrag at line 13


  protected BytecodeReader bytecodeReader;

    // Declared in ClassPath.jrag at line 14

  public void initBytecodeReader(BytecodeReader r) { bytecodeReader = r; }

    // Declared in ClassPath.jrag at line 15

  protected JavaParser javaParser;

    // Declared in ClassPath.jrag at line 16

  public void initJavaParser(JavaParser p) { javaParser = p; }

    // Declared in ClassPath.jrag at line 23


  // add a filename to the list of source files to process
  public void addSourceFile(String name) {
    sourceFiles.addSourceFile(name);

  }

    // Declared in ClassPath.jrag at line 29


  // iterate over all source files and demand-loaded compilation units
  public Iterator compilationUnitIterator() {
    initPaths();
    return new Iterator() {
      int index = 0;
      public boolean hasNext() {
        return index < getNumCompilationUnit() || !sourceFiles.isEmpty();
      }
      public Object next() {
        if(getNumCompilationUnit() == index) {
          String typename = (String)sourceFiles.keySet().iterator().next();
          CompilationUnit u = getCompilationUnit(typename);
          if(u != null) {
            addCompilationUnit(u);
            getCompilationUnit(getNumCompilationUnit()-1);
          }
          else
            throw new Error("File " + typename + " not found");
        }
        return getCompilationUnit(index++);
      }
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

    // Declared in ClassPath.jrag at line 58

  
  // get the input stream for a compilation unit specified using
  // a canonical name. This is used by the bytecode reader to load
  // nested types
  public InputStream getInputStream(String name) {
    initPaths();
    try {
      for(Iterator iter = classPath.iterator(); iter.hasNext(); ) {
        PathPart part = (PathPart)iter.next();
        if(part.selectCompilationUnit(name))
          return part.is;
      }
    }
    catch(IOException e) {
    }
    throw new Error("Could not find nested type " + name);
  }

    // Declared in ClassPath.jrag at line 76

    
  // load a compilation unit from disc using the following rules:
  //   1) specified on the command line
  //   2) class file not older than source file
  //   3) source file
  public CompilationUnit getCompilationUnit(String name) {
    initPaths();
    try {
      if(sourceFiles.selectCompilationUnit(name))
        return sourceFiles.getCompilationUnit();
      PathPart sourcePart = null;
      PathPart classPart = null;
      for(Iterator iter = sourcePath.iterator(); iter.hasNext() && sourcePart == null; ) {
        PathPart part = (PathPart)iter.next();
        if(part.selectCompilationUnit(name))
          sourcePart = part;
      }
      for(Iterator iter = classPath.iterator(); iter.hasNext() && classPart == null; ) {
        PathPart part = (PathPart)iter.next();
        if(part.selectCompilationUnit(name))
          classPart = part;
      }
      
      if(sourcePart != null && (classPart == null || classPart.age <= sourcePart.age)) {
        CompilationUnit unit = sourcePart.getCompilationUnit();
        int index = name.lastIndexOf('.');
        if(index == -1)
          return unit;
        String pkgName = name.substring(0, index);
        if(pkgName.equals(unit.getPackageDecl()))
          return unit;
      }
      if(classPart != null) {
        CompilationUnit unit = classPart.getCompilationUnit();
        int index = name.lastIndexOf('.');
        if(index == -1)
          return unit;
        String pkgName = name.substring(0, index);
        if(pkgName.equals(unit.getPackageDecl()))
          return unit;
      }
      return null;
    }
    catch(IOException e) {
    }
    return null;
  }

    // Declared in ClassPath.jrag at line 120

  
  // is there a package named name on the path
  public boolean isPackage(String name) {
    if(sourceFiles.hasPackage(name))
      return true;
    for(Iterator iter = classPath.iterator(); iter.hasNext(); ) {
      PathPart part = (PathPart)iter.next();
      if(part.hasPackage(name))
        return true;
    }
    for(Iterator iter = sourcePath.iterator(); iter.hasNext(); ) {
      PathPart part = (PathPart)iter.next();
      if(part.hasPackage(name))
        return true;
    }
    return false;
  }

    // Declared in ClassPath.jrag at line 150


  private boolean pathsInitialized = false;

    // Declared in ClassPath.jrag at line 151

  private java.util.ArrayList classPath;

    // Declared in ClassPath.jrag at line 152

  private java.util.ArrayList sourcePath;

    // Declared in ClassPath.jrag at line 153

  private FileNamesPart sourceFiles = new FileNamesPart(this);

    // Declared in ClassPath.jrag at line 155


  public void pushClassPath(String name) {
    PathPart part = PathPart.createSourcePath(name, this);
    if(part != null) {
      sourcePath.add(part);
      System.out.println("Pushing source path " + name);
    }
    else
      throw new Error("Could not push source path " + name);
    part = PathPart.createClassPath(name, this);
    if(part != null) {
      classPath.add(part);
      System.out.println("Pushing class path " + name);
    }
  }

    // Declared in ClassPath.jrag at line 169

  public void popClassPath() {
    if(sourcePath.size() > 0)
      sourcePath.remove(sourcePath.size()-1);
    if(classPath.size() > 0)
      classPath.remove(classPath.size()-1);
  }

    // Declared in ClassPath.jrag at line 176


  public void initPaths() {
    if(!pathsInitialized) {
      pathsInitialized = true;

      //System.err.println("Initializing class paths");
      
      ArrayList classPaths = new ArrayList();
      ArrayList sourcePaths = new ArrayList();
      
      String[] bootclasspaths;
      if(Program.hasValueForOption("-bootclasspath"))
        bootclasspaths = Program.getValueForOption("-bootclasspath").split(File.pathSeparator);
      else
        bootclasspaths = System.getProperty("sun.boot.class.path").split(File.pathSeparator);
      for(int i = 0; i < bootclasspaths.length; i++) {
        classPaths.add(bootclasspaths[i]);
        //System.err.println("Adding classpath " + bootclasspaths[i]);
      }
      
      String[] extdirs;
      if(Program.hasValueForOption("-extdirs"))
        extdirs = Program.getValueForOption("-extdirs").split(File.pathSeparator);
      else
        extdirs = System.getProperty("java.ext.dirs").split(File.pathSeparator);
      for(int i = 0; i < extdirs.length; i++) {
        classPaths.add(extdirs[i]);
        //System.err.println("Adding classpath " + extdirs[i]);
      }

      String[] userClasses = null;
      if(Program.hasValueForOption("-classpath"))
        userClasses = Program.getValueForOption("-classpath").split(File.pathSeparator);
      else {
        String s = System.getProperty("java.class.path");
        if(s != null && s.length() > 0) {
          s = s + File.pathSeparator + "."; // TODO; This should not be necessary
          userClasses = s.split(File.pathSeparator);
        }
        else
          userClasses = ".".split(File.pathSeparator);
      }
      if(!Program.hasValueForOption("-sourcepath")) {
        for(int i = 0; i < userClasses.length; i++) {
          classPaths.add(userClasses[i]);
          sourcePaths.add(userClasses[i]);
          //System.err.println("Adding classpath/sourcepath " + userClasses[i]);
        }
      }
      else {
        for(int i = 0; i < userClasses.length; i++) {
          classPaths.add(userClasses[i]);
          //System.err.println("Adding classpath " + userClasses[i]);
        }
        userClasses = Program.getValueForOption("-sourcepath").split(File.pathSeparator);
        for(int i = 0; i < userClasses.length; i++) {
          sourcePaths.add(userClasses[i]);
          //System.err.println("Adding sourcepath " + userClasses[i]);
        }
      }
        
      classPath = new ArrayList();
      sourcePath = new ArrayList();
      
      for(Iterator iter = classPaths.iterator(); iter.hasNext(); ) {
        String s = (String)iter.next();
        PathPart part = PathPart.createClassPath(s, this);
        if(part != null) {
          classPath.add(part);
          //System.out.println("Adding classpath " + s);
        }
        else if(Program.verbose())
          System.out.println("Warning: Could not use " + s + " as class path");
      }
      for(Iterator iter = sourcePaths.iterator(); iter.hasNext(); ) {
        String s = (String)iter.next();
        PathPart part = PathPart.createSourcePath(s, this);
        if(part != null) {
          sourcePath.add(part);
          //System.out.println("Adding sourcepath " + s);
        }
        else if(Program.verbose())
          System.out.println("Warning: Could not use " + s + " as source path");
      }
    }
  }

    // Declared in ClassPath.jrag at line 529


  // remove user defined classes from this program but keep library classes
  public void simpleReset() {
    lookupType_String_String_values = new HashMap();
    hasPackage_String_values = new HashMap();
    List list = new List();
    for(int i = 0; i < getNumCompilationUnit(); i++) {
      CompilationUnit unit = getCompilationUnit(i);
      if(!unit.fromSource()) {
        list.add(unit);
      }
    }
    setCompilationUnitList(list);
  }

    // Declared in ErrorCheck.jrag at line 206


  public void errorCheck(Collection collection) {
    for(Iterator iter = compilationUnitIterator(); iter.hasNext(); ) {
      CompilationUnit cu = (CompilationUnit)iter.next();
      if(cu.fromSource()) {
        cu.collectErrors();
        collection.addAll(cu.errors);
      }
    }
  }

    // Declared in ErrorCheck.jrag at line 215

  public void errorCheck(Collection collection, Collection warn) {
    for(Iterator iter = compilationUnitIterator(); iter.hasNext(); ) {
      CompilationUnit cu = (CompilationUnit)iter.next();
      if(cu.fromSource()) {
        cu.collectErrors();
        collection.addAll(cu.errors);
        warn.addAll(cu.warnings);
      }
    }
  }

    // Declared in ErrorCheck.jrag at line 236

  
  public boolean errorCheck() {
    Collection collection = new LinkedList();
    errorCheck(collection);
    if(collection.isEmpty())
      return false;
    System.out.println("Errors:");
    for(Iterator iter = collection.iterator(); iter.hasNext(); ) {
      String s = (String)iter.next();
      System.out.println(s);
    }
    return true;
  }

    // Declared in LookupType.jrag at line 93


  public int classFileReadTime;

    // Declared in Options.jadd at line 14

  private static Map options = new HashMap();

    // Declared in Options.jadd at line 15

  private static Map optionDescriptions = new HashMap();

    // Declared in Options.jadd at line 17


  private HashSet files = new HashSet();

    // Declared in Options.jadd at line 18

  public Collection files() {
    return files;
  }

    // Declared in Options.jadd at line 22


  public static void initOptions() {
    options = new HashMap();
    optionDescriptions = new HashMap();
  }

    // Declared in Options.jadd at line 27


  public void addKeyOption(String name) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, false, false));
  }

    // Declared in Options.jadd at line 33

  
  public void addKeyValueOption(String name) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, true, false));
  }

    // Declared in Options.jadd at line 39

  
  public void addKeyCollectionOption(String name) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, true, true));
  }

    // Declared in Options.jadd at line 45

 
  public void addOptionDescription(String name, boolean value) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, value, false));
  }

    // Declared in Options.jadd at line 50

  public void addOptionDescription(String name, boolean value, boolean isCollection) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, value, isCollection));
  }

    // Declared in Options.jadd at line 56

  
  public void addOptions(String[] args) {
    for(int i = 0; i < args.length; i++) {
      String arg = args[i];
      if(arg.startsWith("@")) {
        try {
          String fileName = arg.substring(1,arg.length());
          java.io.StreamTokenizer tokenizer = new java.io.StreamTokenizer(new java.io.FileReader(fileName));
          tokenizer.resetSyntax();
          tokenizer.whitespaceChars(' ',' ');
          tokenizer.whitespaceChars('\t','\t');
          tokenizer.whitespaceChars('\f','\f');
          tokenizer.whitespaceChars('\n','\n');
          tokenizer.whitespaceChars('\r','\r');
          tokenizer.wordChars(33,255);
          ArrayList list = new ArrayList();
          int next = tokenizer.nextToken();
          while(next != java.io.StreamTokenizer.TT_EOF) {
            if(next == java.io.StreamTokenizer.TT_WORD) {
              list.add(tokenizer.sval);
            }
            next = tokenizer.nextToken();
          }
          String[] newArgs = new String[list.size()];
          int index = 0;
          for(Iterator iter = list.iterator(); iter.hasNext(); index++) {
            newArgs[index] = (String)iter.next();
          }
          addOptions(newArgs);
        } catch (java.io.IOException e) {
        }
      }
      else if(arg.startsWith("-")) {
        if(!optionDescriptions.containsKey(arg))
          throw new Error("Command line argument error: option " + arg + " is not defined");
        Option o = (Option)optionDescriptions.get(arg);
        
        if(!o.isCollection && options.containsKey(arg))
          throw new Error("Command line argument error: option " + arg + " is multiply defined");
        
        if(o.hasValue && !o.isCollection) {
          String value = null;
          if(i + 1 > args.length - 1)
            throw new Error("Command line argument error: value missing for key " + arg);
          value = args[i+1];
          if(value.startsWith("-"))
            throw new Error("Command line argument error: value missing for key " + arg);
          i++;
          options.put(arg, value);
        }
        else if(o.hasValue && o.isCollection) {
          String value = null;
          if(i + 1 > args.length - 1)
            throw new Error("Command line argument error: value missing for key " + arg);
          value = args[i+1];
          if(value.startsWith("-"))
            throw new Error("Command line argument error: value missing for key " + arg);
          i++;
          Collection c = (Collection)options.get(arg);
          if(c == null)
            c = new ArrayList();
          c.add(value);
          options.put(arg, c);
        }
        else {
          options.put(arg, null);
        }
      }
      else {
        files.add(arg);
      }
    }
  }

    // Declared in Options.jadd at line 128

  public static boolean hasOption(String name) {
    return options.containsKey(name);
  }

    // Declared in Options.jadd at line 131

  public static void setOption(String name) {
    options.put(name, null);
  }

    // Declared in Options.jadd at line 134

  public static boolean hasValueForOption(String name) {
    return options.containsKey(name) && options.get(name) != null;
  }

    // Declared in Options.jadd at line 137

  public static String getValueForOption(String name) {
    if(!hasValueForOption(name))
      throw new Error("Command line argument error: key " + name + " does not have a value");
    return (String)options.get(name);
  }

    // Declared in Options.jadd at line 142

  public static void setValueForOption(String value, String option) {
    options.put(option, value);
  }

    // Declared in Options.jadd at line 145

  public static Collection getValueCollectionForOption(String name) {
    if(!hasValueForOption(name))
      throw new Error("Command line argument error: key " + name + " does not have a value");
    return (Collection)options.get(name);
  }

    // Declared in Options.jadd at line 151


  public static boolean verbose() {
    return hasOption("-verbose");
  }

    // Declared in PrettyPrint.jadd at line 25


  public void toString(StringBuffer s) {
    for(Iterator iter = compilationUnitIterator(); iter.hasNext(); ) {
      CompilationUnit cu = (CompilationUnit)iter.next();
      if(cu.fromSource()) { 
        cu.toString(s);
      }
    }
  }

    // Declared in PrettyPrint.jadd at line 953


  public String dumpTree() {
    StringBuffer s = new StringBuffer();
    for(Iterator iter = compilationUnitIterator(); iter.hasNext(); ) {
      CompilationUnit cu = (CompilationUnit)iter.next();
      if(cu.fromSource()) { 
        s.append(cu.dumpTree());
      }
    }
    return s.toString();
  }

    // Declared in PrimitiveTypes.jrag at line 4


  private boolean initPrimTypes = false;

    // Declared in PrimitiveTypes.jrag at line 6

  
  public void addPrimitiveTypes() {
    if(!initPrimTypes) {
      initPrimTypes = true;
    
    CompilationUnit u = new CompilationUnit();
    u.setPackageDecl(PRIMITIVE_PACKAGE_NAME);
    addCompilationUnit(u);

    TypeDecl classDecl = generateUnknownType();
    u.addTypeDecl(classDecl);
    TypeDecl unknown = classDecl;

    classDecl = generatePrimitiveType(new BooleanType(), "boolean", unknown);
    u.addTypeDecl(classDecl);
    
    classDecl = generatePrimitiveType(new DoubleType(), "double", unknown);
    u.addTypeDecl(classDecl);
    
    classDecl = generatePrimitiveType(new FloatType(), "float", classDecl);
    u.addTypeDecl(classDecl);
    
    classDecl = generatePrimitiveType(new LongType(), "long", classDecl);
    u.addTypeDecl(classDecl);
    
    classDecl = generatePrimitiveType(new IntType(), "int", classDecl);
    u.addTypeDecl(classDecl);
    TypeDecl intDecl = classDecl;
    
    classDecl = generatePrimitiveType(new ShortType(), "short", classDecl);
    u.addTypeDecl(classDecl);
    
    classDecl = generatePrimitiveType(new ByteType(), "byte", classDecl);
    u.addTypeDecl(classDecl);
    
    classDecl = generatePrimitiveType(new CharType(), "char", intDecl);
    u.addTypeDecl(classDecl);
    
    classDecl = new NullType();
    classDecl.setModifiers(new Modifiers(new List().add(new Modifier("public"))));
    classDecl.setID("null");
    u.addTypeDecl(classDecl);

    classDecl = new VoidType();
    classDecl.setModifiers(new Modifiers(new List().add(new Modifier("public"))));
    classDecl.setID("void");
    u.addTypeDecl(classDecl);

    }
  }

    // Declared in PrimitiveTypes.jrag at line 56


  public TypeDecl generatePrimitiveType(PrimitiveType type, String name, TypeDecl superType) {
    type.setModifiers(new Modifiers(new List().add(new Modifier("public"))));
    type.setID(name);
    if(superType != null)
      type.setSuperClassAccess(superType.createQualifiedAccess());
    return type;
  }

    // Declared in PrimitiveTypes.jrag at line 64


  private TypeDecl generateUnknownType() {
    ClassDecl classDecl = new UnknownType();
    classDecl.setModifiers(new Modifiers(new List().add(new Modifier("public"))));
    classDecl.setID("Unknown");
    MethodDecl methodDecl = new MethodDecl(
        new Modifiers(new List().add(
          new Modifier("public")
        )),
        new PrimitiveTypeAccess("Unknown"),
        "unknown",
        new List(),
        new List(),
        new List(),
        new Opt()
    );
    classDecl.addBodyDecl(methodDecl);
    FieldDeclaration fieldDecl = new FieldDeclaration(
        new Modifiers(new List().add(
          new Modifier("public")
        )),
        new PrimitiveTypeAccess("Unknown"),
        "unknown",
        new Opt()
    );
    classDecl.addBodyDecl(fieldDecl);   
    ConstructorDecl constrDecl = new ConstructorDecl(
      new Modifiers(new List().add(new Modifier("public"))),
      "Unknown",
      new List(),
      new List(),
      new Opt(),
      new Block()
    );
    classDecl.addBodyDecl(constrDecl);
      
    return classDecl;
  }

    // Declared in Undo.jadd at line 5


	private Stack undoStack = new Stack();

    // Declared in Undo.jadd at line 6

	public Iterator getUndoIterator() { return undoStack.iterator(); }

    // Declared in Undo.jadd at line 7

	public void pushUndo(ASTChange ch) { undoStack.push(ch); }

    // Declared in Undo.jadd at line 9

	
	public void undo() {
		while(!undoStack.empty())
			((ASTChange)undoStack.pop()).undo();
	}

    // Declared in java.ast at line 3
    // Declared in java.ast line 1

    public Program() {
        super();

        setChild(new List(), 0);
        is$Final(true);

    }

    // Declared in java.ast at line 12


    // Declared in java.ast line 1
    public Program(List p0) {
        setChild(p0, 0);
        is$Final(true);
    }

    // Declared in java.ast at line 17


  protected int numChildren() {
    return 1;
  }

    // Declared in java.ast at line 20

  public boolean mayHaveRewrite() { return false; }

    // Declared in java.ast at line 2
    // Declared in java.ast line 1
    public void setCompilationUnitList(List list) {
        setChild(list, 0);
    }

    // Declared in java.ast at line 6


    private int getNumCompilationUnit = 0;

    // Declared in java.ast at line 7

    public int getNumCompilationUnit() {
        return getCompilationUnitList().getNumChild();
    }

    // Declared in java.ast at line 11


    public CompilationUnit getCompilationUnit(int i) {
        return (CompilationUnit)getCompilationUnitList().getChild(i);
    }

    // Declared in java.ast at line 15


    public void addCompilationUnit(CompilationUnit node) {
        List list = getCompilationUnitList();
        list.addChild(node);
    }

    // Declared in java.ast at line 20


    public void setCompilationUnit(CompilationUnit node, int i) {
        List list = getCompilationUnitList();
        list.setChild(node, i);
    }

    // Declared in java.ast at line 24

    public List getCompilationUnitList() {
        return (List)getChild(0);
    }

    // Declared in java.ast at line 28


    public List getCompilationUnitListNoTransform() {
        return (List)getChildNoTransform(0);
    }

    // Declared in Uses.jrag at line 72
    private boolean collect_contributors_TypeDecl_uses = false;
    protected void collect_contributors_TypeDecl_uses() {
        if(collect_contributors_TypeDecl_uses) return;
        super.collect_contributors_TypeDecl_uses();
        collect_contributors_TypeDecl_uses = true;
    }



    // Declared in Uses.jrag at line 25
    private boolean collect_contributors_FieldDeclaration_uses = false;
    protected void collect_contributors_FieldDeclaration_uses() {
        if(collect_contributors_FieldDeclaration_uses) return;
        super.collect_contributors_FieldDeclaration_uses();
        collect_contributors_FieldDeclaration_uses = true;
    }



    // Declared in Uses.jrag at line 30
    private boolean collect_contributors_VariableDeclaration_uses = false;
    protected void collect_contributors_VariableDeclaration_uses() {
        if(collect_contributors_VariableDeclaration_uses) return;
        super.collect_contributors_VariableDeclaration_uses();
        collect_contributors_VariableDeclaration_uses = true;
    }



    // Declared in Uses.jrag at line 35
    private boolean collect_contributors_ParameterDeclaration_uses = false;
    protected void collect_contributors_ParameterDeclaration_uses() {
        if(collect_contributors_ParameterDeclaration_uses) return;
        super.collect_contributors_ParameterDeclaration_uses();
        collect_contributors_ParameterDeclaration_uses = true;
    }



    // Declared in Uses.jrag at line 44
    private boolean collect_contributors_MethodDecl_definiteUses = false;
    protected void collect_contributors_MethodDecl_definiteUses() {
        if(collect_contributors_MethodDecl_definiteUses) return;
        super.collect_contributors_MethodDecl_definiteUses();
        collect_contributors_MethodDecl_definiteUses = true;
    }



    // Declared in Uses.jrag at line 48
    private boolean collect_contributors_MethodDecl_uses = false;
    protected void collect_contributors_MethodDecl_uses() {
        if(collect_contributors_MethodDecl_uses) return;
        super.collect_contributors_MethodDecl_uses();
        collect_contributors_MethodDecl_uses = true;
    }



    // Declared in Uses.jrag at line 60
    private boolean collect_contributors_MethodDecl_overriders = false;
    protected void collect_contributors_MethodDecl_overriders() {
        if(collect_contributors_MethodDecl_overriders) return;
        super.collect_contributors_MethodDecl_overriders();
        collect_contributors_MethodDecl_overriders = true;
    }



    // Declared in ControlFlowGraph.jrag at line 74
    private boolean collect_contributors_Stmt_pred = false;
    protected void collect_contributors_Stmt_pred() {
        if(collect_contributors_Stmt_pred) return;
        super.collect_contributors_Stmt_pred();
        collect_contributors_Stmt_pred = true;
    }



    // Declared in Uses.jrag at line 89
    private boolean collect_contributors_PackageDecl_prefixUses = false;
    protected void collect_contributors_PackageDecl_prefixUses() {
        if(collect_contributors_PackageDecl_prefixUses) return;
        super.collect_contributors_PackageDecl_prefixUses();
        collect_contributors_PackageDecl_prefixUses = true;
    }



    protected boolean typeObject_computed = false;
    protected TypeDecl typeObject_value;
    // Declared in LookupType.jrag at line 6
    public TypeDecl typeObject() {
        if(typeObject_computed)
            return typeObject_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeObject_value = typeObject_compute();
        if(isFinal && num == boundariesCrossed)
            typeObject_computed = true;
        return typeObject_value;
    }

    private TypeDecl typeObject_compute() {  return  lookupType("java.lang", "Object");  }

    protected boolean typeCloneable_computed = false;
    protected TypeDecl typeCloneable_value;
    // Declared in LookupType.jrag at line 7
    public TypeDecl typeCloneable() {
        if(typeCloneable_computed)
            return typeCloneable_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeCloneable_value = typeCloneable_compute();
        if(isFinal && num == boundariesCrossed)
            typeCloneable_computed = true;
        return typeCloneable_value;
    }

    private TypeDecl typeCloneable_compute() {  return  lookupType("java.lang", "Cloneable");  }

    protected boolean typeSerializable_computed = false;
    protected TypeDecl typeSerializable_value;
    // Declared in LookupType.jrag at line 8
    public TypeDecl typeSerializable() {
        if(typeSerializable_computed)
            return typeSerializable_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeSerializable_value = typeSerializable_compute();
        if(isFinal && num == boundariesCrossed)
            typeSerializable_computed = true;
        return typeSerializable_value;
    }

    private TypeDecl typeSerializable_compute() {  return  lookupType("java.io", "Serializable");  }

    protected boolean typeBoolean_computed = false;
    protected TypeDecl typeBoolean_value;
    // Declared in LookupType.jrag at line 13
    public TypeDecl typeBoolean() {
        if(typeBoolean_computed)
            return typeBoolean_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeBoolean_value = typeBoolean_compute();
        if(isFinal && num == boundariesCrossed)
            typeBoolean_computed = true;
        return typeBoolean_value;
    }

    private TypeDecl typeBoolean_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME, "boolean");  }

    protected boolean typeByte_computed = false;
    protected TypeDecl typeByte_value;
    // Declared in LookupType.jrag at line 14
    public TypeDecl typeByte() {
        if(typeByte_computed)
            return typeByte_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeByte_value = typeByte_compute();
        if(isFinal && num == boundariesCrossed)
            typeByte_computed = true;
        return typeByte_value;
    }

    private TypeDecl typeByte_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME , "byte");  }

    protected boolean typeShort_computed = false;
    protected TypeDecl typeShort_value;
    // Declared in LookupType.jrag at line 15
    public TypeDecl typeShort() {
        if(typeShort_computed)
            return typeShort_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeShort_value = typeShort_compute();
        if(isFinal && num == boundariesCrossed)
            typeShort_computed = true;
        return typeShort_value;
    }

    private TypeDecl typeShort_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME , "short");  }

    protected boolean typeChar_computed = false;
    protected TypeDecl typeChar_value;
    // Declared in LookupType.jrag at line 16
    public TypeDecl typeChar() {
        if(typeChar_computed)
            return typeChar_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeChar_value = typeChar_compute();
        if(isFinal && num == boundariesCrossed)
            typeChar_computed = true;
        return typeChar_value;
    }

    private TypeDecl typeChar_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME , "char");  }

    protected boolean typeInt_computed = false;
    protected TypeDecl typeInt_value;
    // Declared in LookupType.jrag at line 17
    public TypeDecl typeInt() {
        if(typeInt_computed)
            return typeInt_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeInt_value = typeInt_compute();
        if(isFinal && num == boundariesCrossed)
            typeInt_computed = true;
        return typeInt_value;
    }

    private TypeDecl typeInt_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME , "int");  }

    protected boolean typeLong_computed = false;
    protected TypeDecl typeLong_value;
    // Declared in LookupType.jrag at line 18
    public TypeDecl typeLong() {
        if(typeLong_computed)
            return typeLong_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeLong_value = typeLong_compute();
        if(isFinal && num == boundariesCrossed)
            typeLong_computed = true;
        return typeLong_value;
    }

    private TypeDecl typeLong_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME , "long");  }

    protected boolean typeFloat_computed = false;
    protected TypeDecl typeFloat_value;
    // Declared in LookupType.jrag at line 19
    public TypeDecl typeFloat() {
        if(typeFloat_computed)
            return typeFloat_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeFloat_value = typeFloat_compute();
        if(isFinal && num == boundariesCrossed)
            typeFloat_computed = true;
        return typeFloat_value;
    }

    private TypeDecl typeFloat_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME , "float");  }

    protected boolean typeDouble_computed = false;
    protected TypeDecl typeDouble_value;
    // Declared in LookupType.jrag at line 20
    public TypeDecl typeDouble() {
        if(typeDouble_computed)
            return typeDouble_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeDouble_value = typeDouble_compute();
        if(isFinal && num == boundariesCrossed)
            typeDouble_computed = true;
        return typeDouble_value;
    }

    private TypeDecl typeDouble_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME , "double");  }

    protected boolean typeString_computed = false;
    protected TypeDecl typeString_value;
    // Declared in LookupType.jrag at line 21
    public TypeDecl typeString() {
        if(typeString_computed)
            return typeString_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeString_value = typeString_compute();
        if(isFinal && num == boundariesCrossed)
            typeString_computed = true;
        return typeString_value;
    }

    private TypeDecl typeString_compute() {  return  lookupType("java.lang", "String");  }

    protected boolean typeVoid_computed = false;
    protected TypeDecl typeVoid_value;
    // Declared in LookupType.jrag at line 32
    public TypeDecl typeVoid() {
        if(typeVoid_computed)
            return typeVoid_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeVoid_value = typeVoid_compute();
        if(isFinal && num == boundariesCrossed)
            typeVoid_computed = true;
        return typeVoid_value;
    }

    private TypeDecl typeVoid_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME, "void");  }

    protected boolean typeNull_computed = false;
    protected TypeDecl typeNull_value;
    // Declared in LookupType.jrag at line 34
    public TypeDecl typeNull() {
        if(typeNull_computed)
            return typeNull_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        typeNull_value = typeNull_compute();
        if(isFinal && num == boundariesCrossed)
            typeNull_computed = true;
        return typeNull_value;
    }

    private TypeDecl typeNull_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME, "null");  }

    protected boolean unknownType_computed = false;
    protected TypeDecl unknownType_value;
    // Declared in LookupType.jrag at line 37
    public TypeDecl unknownType() {
        if(unknownType_computed)
            return unknownType_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        unknownType_value = unknownType_compute();
        if(isFinal && num == boundariesCrossed)
            unknownType_computed = true;
        return unknownType_value;
    }

    private TypeDecl unknownType_compute() {  return  lookupType(PRIMITIVE_PACKAGE_NAME, "Unknown");  }

    protected java.util.Map hasPackage_String_values;
    // Declared in LookupType.jrag at line 68
    public boolean hasPackage(String packageName) {
        Object _parameters = packageName;
if(hasPackage_String_values == null) hasPackage_String_values = new java.util.HashMap(4);
        if(hasPackage_String_values.containsKey(_parameters))
            return ((Boolean)hasPackage_String_values.get(_parameters)).booleanValue();
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        boolean hasPackage_String_value = hasPackage_compute(packageName);
        if(isFinal && num == boundariesCrossed)
            hasPackage_String_values.put(_parameters, Boolean.valueOf(hasPackage_String_value));
        return hasPackage_String_value;
    }

    private boolean hasPackage_compute(String packageName)  {
    return isPackage(packageName);
  }

    protected java.util.Map lookupType_String_String_values;
    // Declared in LookupType.jrag at line 95
    public TypeDecl lookupType(String packageName, String typeName) {
        java.util.List _parameters = new java.util.ArrayList(2);
        _parameters.add(packageName);
        _parameters.add(typeName);
if(lookupType_String_String_values == null) lookupType_String_String_values = new java.util.HashMap(4);
        if(lookupType_String_String_values.containsKey(_parameters))
            return (TypeDecl)lookupType_String_String_values.get(_parameters);
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        TypeDecl lookupType_String_String_value = lookupType_compute(packageName, typeName);
        if(isFinal && num == boundariesCrossed)
            lookupType_String_String_values.put(_parameters, lookupType_String_String_value);
        return lookupType_String_String_value;
    }

    private TypeDecl lookupType_compute(String packageName, String typeName)  {
    addPrimitiveTypes();
    String fullName = packageName.equals("") ? typeName : packageName + "." + typeName;
    for(int i = 0; i < getNumCompilationUnit(); i++) {
      for(int j = 0; j < getCompilationUnit(i).getNumTypeDecl(); j++) {
        TypeDecl type = getCompilationUnit(i).getTypeDecl(j);
        if(type.fullName().equals(fullName)) {
          return type;
        }
      }
    }
    
    CompilationUnit u = getCompilationUnit(fullName);
    if(u != null) {
      addCompilationUnit(u);
      getCompilationUnit(getNumCompilationUnit()-1);
      for(int j = 0; j < u.getNumTypeDecl(); j++) {
        if(u.getTypeDecl(j).name().equals(typeName)) {
          return u.getTypeDecl(j);
        }
      }
      throw new Error("No type named " + typeName + " in file " + fullName + ", " + u.pathName() + ", " + u.relativeName());
    }
    return null;
  }

    protected boolean unknownConstructor_computed = false;
    protected ConstructorDecl unknownConstructor_value;
    // Declared in TypeAnalysis.jrag at line 237
    public ConstructorDecl unknownConstructor() {
        if(unknownConstructor_computed)
            return unknownConstructor_value;
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        unknownConstructor_value = unknownConstructor_compute();
        if(isFinal && num == boundariesCrossed)
            unknownConstructor_computed = true;
        return unknownConstructor_value;
    }

    private ConstructorDecl unknownConstructor_compute()  {
    return (ConstructorDecl)unknownType().constructors().iterator().next();
  }

    protected java.util.Map getPackageDecl_String_values;
    protected List getPackageDecl_String_list;
    // Declared in Uses.jrag at line 98
    public PackageDecl getPackageDecl(String name) {
        Object _parameters = name;
if(getPackageDecl_String_values == null) getPackageDecl_String_values = new java.util.HashMap(4);
        if(getPackageDecl_String_values.containsKey(_parameters))
            return (PackageDecl)getPackageDecl_String_values.get(_parameters);
        int num = boundariesCrossed;
        boolean isFinal = this.is$Final();
        PackageDecl getPackageDecl_String_value = getPackageDecl_compute(name);
        if(getPackageDecl_String_list == null) {
            getPackageDecl_String_list = new List();
            getPackageDecl_String_list.is$Final = true;
            getPackageDecl_String_list.setParent(this);
        }
        getPackageDecl_String_list.add(getPackageDecl_String_value);
        getPackageDecl_String_value.is$Final = true;
        if(true)
            getPackageDecl_String_values.put(_parameters, getPackageDecl_String_value);
        return getPackageDecl_String_value;
    }

    private PackageDecl getPackageDecl_compute(String name)  {
		// the following gives null-pointer exceptions in generated code
		//return hasPackage(name) ? new PackageDecl(name) : null;
		return new PackageDecl(name);
	}

    // Declared in Modifiers.jrag at line 277
    public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBePrivate(this, caller);
    }

    // Declared in LookupType.jrag at line 38
    public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  unknownType();
        }
        return getParent().Define_TypeDecl_unknownType(this, caller);
    }

    // Declared in GuardedControlFlow.jrag at line 26
    public boolean Define_boolean_between(ASTNode caller, ASTNode child, Block blk, int start, int end) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_between(this, caller, blk, start, end);
    }

    // Declared in LookupType.jrag at line 11
    public TypeDecl Define_TypeDecl_typeSerializable(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeSerializable();
        }
        return getParent().Define_TypeDecl_typeSerializable(this, caller);
    }

    // Declared in ExceptionHandling.jrag at line 105
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if(true) { 
   int childIndex = this.getIndexOfChild(caller);
 {
    throw new Error("Operation handlesException not supported");
  }
}
        return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }

    // Declared in ExceptionHandling.jrag at line 10
    public TypeDecl Define_TypeDecl_typeNullPointerException(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  lookupType("java.lang", "NullPointerException");
        }
        return getParent().Define_TypeDecl_typeNullPointerException(this, caller);
    }

    // Declared in LookupConstructor.jrag at line 6
    public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  Collections.EMPTY_LIST;
        }
        return getParent().Define_Collection_lookupConstructor(this, caller);
    }

    // Declared in Modifiers.jrag at line 279
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeFinal(this, caller);
    }

    // Declared in ExceptionHandling.jrag at line 12
    public TypeDecl Define_TypeDecl_typeThrowable(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  lookupType("java.lang", "Throwable");
        }
        return getParent().Define_TypeDecl_typeThrowable(this, caller);
    }

    // Declared in TypeAnalysis.jrag at line 581
    public BodyDecl Define_BodyDecl_hostBodyDecl(ASTNode caller, ASTNode child) {
        if(true) { 
   int childIndex = this.getIndexOfChild(caller);
 { throw new UnsupportedOperationException(); }
}
        return getParent().Define_BodyDecl_hostBodyDecl(this, caller);
    }

    // Declared in NameCheck.jrag at line 358
    public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_insideLoop(this, caller);
    }

    // Declared in BranchTarget.jrag at line 164
    public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_LabeledStmt_lookupLabel(this, caller, name);
    }

    // Declared in Modifiers.jrag at line 275
    public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBePublic(this, caller);
    }

    // Declared in LookupConstructor.jrag at line 15
    public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  Collections.EMPTY_LIST;
        }
        return getParent().Define_Collection_lookupSuperConstructor(this, caller);
    }

    // Declared in LookupType.jrag at line 27
    public TypeDecl Define_TypeDecl_typeLong(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeLong();
        }
        return getParent().Define_TypeDecl_typeLong(this, caller);
    }

    // Declared in ExceptionHandling.jrag at line 6
    public TypeDecl Define_TypeDecl_typeRuntimeException(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  lookupType("java.lang", "RuntimeException");
        }
        return getParent().Define_TypeDecl_typeRuntimeException(this, caller);
    }

    // Declared in ExceptionHandling.jrag at line 8
    public TypeDecl Define_TypeDecl_typeError(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  lookupType("java.lang", "Error");
        }
        return getParent().Define_TypeDecl_typeError(this, caller);
    }

    // Declared in Modifiers.jrag at line 276
    public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeProtected(this, caller);
    }

    // Declared in Modifiers.jrag at line 282
    public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeTransient(this, caller);
    }

    // Declared in TypeAnalysis.jrag at line 525
    public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_isMemberType(this, caller);
    }

    // Declared in Modifiers.jrag at line 285
    public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeNative(this, caller);
    }

    // Declared in TypeAnalysis.jrag at line 229
    public MethodDecl Define_MethodDecl_unknownMethod(ASTNode caller, ASTNode child) {
        if(true) { 
   int childIndex = this.getIndexOfChild(caller);
 {
    for(Iterator iter = unknownType().memberMethods("unknown").iterator(); iter.hasNext(); ) {
      MethodDecl m = (MethodDecl)iter.next();
      return m;
    }
    throw new Error("Could not find method unknown in type Unknown");
  }
}
        return getParent().Define_MethodDecl_unknownMethod(this, caller);
    }

    // Declared in LookupType.jrag at line 22
    public TypeDecl Define_TypeDecl_typeBoolean(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeBoolean();
        }
        return getParent().Define_TypeDecl_typeBoolean(this, caller);
    }

    // Declared in Arrays.jrag at line 11
    public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  unknownType();
        }
        return getParent().Define_TypeDecl_componentType(this, caller);
    }

    // Declared in SyntacticClassification.jrag at line 54
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  NameType.NO_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    // Declared in LookupType.jrag at line 35
    public TypeDecl Define_TypeDecl_typeNull(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeNull();
        }
        return getParent().Define_TypeDecl_typeNull(this, caller);
    }

    // Declared in TypeHierarchyCheck.jrag at line 128
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    // Declared in DefiniteAssignment.jrag at line 707
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  true;
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    // Declared in LookupType.jrag at line 30
    public TypeDecl Define_TypeDecl_typeString(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeString();
        }
        return getParent().Define_TypeDecl_typeString(this, caller);
    }

    // Declared in TypeAnalysis.jrag at line 225
    public Variable Define_Variable_unknownField(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  unknownType().findSingleVariable("unknown");
        }
        return getParent().Define_Variable_unknownField(this, caller);
    }

    // Declared in LookupType.jrag at line 26
    public TypeDecl Define_TypeDecl_typeInt(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeInt();
        }
        return getParent().Define_TypeDecl_typeInt(this, caller);
    }

    // Declared in Modifiers.jrag at line 283
    public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeStrictfp(this, caller);
    }

    // Declared in TypeCheck.jrag at line 349
    public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  unknownType();
        }
        return getParent().Define_TypeDecl_switchType(this, caller);
    }

    // Declared in LookupType.jrag at line 9
    public TypeDecl Define_TypeDecl_typeObject(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeObject();
        }
        return getParent().Define_TypeDecl_typeObject(this, caller);
    }

    // Declared in DefiniteAssignment.jrag at line 315
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    // Declared in NameCheck.jrag at line 365
    public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_insideSwitch(this, caller);
    }

    // Declared in TypeHierarchyCheck.jrag at line 119
    public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
    }

    // Declared in LookupType.jrag at line 23
    public TypeDecl Define_TypeDecl_typeByte(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeByte();
        }
        return getParent().Define_TypeDecl_typeByte(this, caller);
    }

    // Declared in LookupType.jrag at line 24
    public TypeDecl Define_TypeDecl_typeShort(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeShort();
        }
        return getParent().Define_TypeDecl_typeShort(this, caller);
    }

    // Declared in LookupType.jrag at line 10
    public TypeDecl Define_TypeDecl_typeCloneable(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeCloneable();
        }
        return getParent().Define_TypeDecl_typeCloneable(this, caller);
    }

    // Declared in ASTUtil.jrag at line 4
    public Program Define_Program_programRoot(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  this;
        }
        return getParent().Define_Program_programRoot(this, caller);
    }

    // Declared in TypeCheck.jrag at line 395
    public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeVoid();
        }
        return getParent().Define_TypeDecl_returnType(this, caller);
    }

    // Declared in AnonymousClasses.jrag at line 7
    public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_TypeDecl_superType(this, caller);
    }

    // Declared in TypeHierarchyCheck.jrag at line 5
    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        if(true) { 
   int childIndex = this.getIndexOfChild(caller);
 {
    throw new Error("Needs extra equation for methodHost()");
  }
}
        return getParent().Define_String_methodHost(this, caller);
    }

    // Declared in LookupType.jrag at line 178
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  SimpleSet.emptySet;
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    // Declared in Modifiers.jrag at line 278
    public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeStatic(this, caller);
    }

    // Declared in LookupType.jrag at line 28
    public TypeDecl Define_TypeDecl_typeFloat(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeFloat();
        }
        return getParent().Define_TypeDecl_typeFloat(this, caller);
    }

    // Declared in LookupMethod.jrag at line 21
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  Collections.EMPTY_LIST;
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    // Declared in TypeAnalysis.jrag at line 572
    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_TypeDecl_hostType(this, caller);
    }

    // Declared in DefiniteAssignment.jrag at line 7
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_isDest(this, caller);
    }

    // Declared in DefiniteAssignment.jrag at line 17
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    // Declared in TypeAnalysis.jrag at line 236
    public ConstructorDecl Define_ConstructorDecl_unknownConstructor(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  unknownConstructor();
        }
        return getParent().Define_ConstructorDecl_unknownConstructor(this, caller);
    }

    // Declared in AnonymousClasses.jrag at line 17
    public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_ConstructorDecl_constructorDecl(this, caller);
    }

    // Declared in Uses.jrag at line 95
    public PackageDecl Define_PackageDecl_findPackageDecl(ASTNode caller, ASTNode child, String name) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  getPackageDecl(name);
        }
        return getParent().Define_PackageDecl_findPackageDecl(this, caller, name);
    }

    // Declared in AccessMethod.jrag at line 10
    public Access Define_Access_accessMethod(ASTNode caller, ASTNode child, MethodDecl md, List args) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_Access_accessMethod(this, caller, md, args);
    }

    // Declared in TypeAnalysis.jrag at line 253
    public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
        if(true) {
      int i = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_TypeDecl_declType(this, caller);
    }

    // Declared in LookupMethod.jrag at line 12
    public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
        if(true) { 
   int childIndex = this.getIndexOfChild(caller);
 { throw new UnsupportedOperationException(); }
}
        return getParent().Define_Expr_nestedScope(this, caller);
    }

    // Declared in LookupType.jrag at line 72
    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  hasPackage(packageName);
        }
        return getParent().Define_boolean_hasPackage(this, caller, packageName);
    }

    // Declared in Domination.jrag at line 67
    public boolean Define_boolean_isInitOrUpdateStmt(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_isInitOrUpdateStmt(this, caller);
    }

    // Declared in LookupType.jrag at line 33
    public TypeDecl Define_TypeDecl_typeVoid(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeVoid();
        }
        return getParent().Define_TypeDecl_typeVoid(this, caller);
    }

    // Declared in AccessType.jrag at line 12
    public Access Define_Access_accessType(ASTNode caller, ASTNode child, TypeDecl td, boolean ambiguous) {
        if(true) { 
   int childIndex = this.getIndexOfChild(caller);
 {
		for(int i = 0; i < getNumCompilationUnit(); i++) {
			for(int j = 0; j < getCompilationUnit(i).getNumTypeDecl(); j++) {
				TypeDecl type = getCompilationUnit(i).getTypeDecl(j);
				if(type == td)
					return new TypeAccess(getCompilationUnit(i).packageName(), td.getID());
			}
		}
		return null;
	}
}
        return getParent().Define_Access_accessType(this, caller, td, ambiguous);
    }

    // Declared in LookupVariable.jrag at line 15
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  SimpleSet.emptySet;
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    // Declared in LookupType.jrag at line 25
    public TypeDecl Define_TypeDecl_typeChar(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeChar();
        }
        return getParent().Define_TypeDecl_typeChar(this, caller);
    }

    // Declared in NameCheck.jrag at line 415
    public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_Case_bind(this, caller, c);
    }

    // Declared in LookupType.jrag at line 91
    public TypeDecl Define_TypeDecl_lookupType(ASTNode caller, ASTNode child, String packageName, String typeName) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  lookupType(packageName, typeName);
        }
        return getParent().Define_TypeDecl_lookupType(this, caller, packageName, typeName);
    }

    // Declared in Modifiers.jrag at line 280
    public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeAbstract(this, caller);
    }

    // Declared in AccessField.jrag at line 88
    public Access Define_Access_accessField(ASTNode caller, ASTNode child, FieldDeclaration fd) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_Access_accessField(this, caller, fd);
    }

    // Declared in TypeAnalysis.jrag at line 211
    public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_isAnonymous(this, caller);
    }

    // Declared in ExceptionHandling.jrag at line 4
    public TypeDecl Define_TypeDecl_typeException(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  lookupType("java.lang", "Exception");
        }
        return getParent().Define_TypeDecl_typeException(this, caller);
    }

    // Declared in Modifiers.jrag at line 281
    public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeVolatile(this, caller);
    }

    // Declared in NameCheck.jrag at line 292
    public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_BodyDecl_enclosingBodyDecl(this, caller);
    }

    // Declared in NameCheck.jrag at line 232
    public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_ASTNode_enclosingBlock(this, caller);
    }

    // Declared in Modifiers.jrag at line 284
    public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_mayBeSynchronized(this, caller);
    }

    // Declared in UnreachableStatements.jrag at line 149
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if(caller == getCompilationUnitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
            return  true;
        }
        return getParent().Define_boolean_reportUnreachable(this, caller);
    }

    // Declared in DefiniteAssignment.jrag at line 39
    public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  false;
        }
        return getParent().Define_boolean_isIncOrDec(this, caller);
    }

    // Declared in TypeCheck.jrag at line 426
    public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  null;
        }
        return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }

    // Declared in NameCheck.jrag at line 283
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        if(true) { 
   int childIndex = this.getIndexOfChild(caller);
 {
    throw new UnsupportedOperationException("outerScope() not defined");
  }
}
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    // Declared in LookupType.jrag at line 29
    public TypeDecl Define_TypeDecl_typeDouble(ASTNode caller, ASTNode child) {
        if(true) {
      int childIndex = this.getIndexOfChild(caller);
            return  typeDouble();
        }
        return getParent().Define_TypeDecl_typeDouble(this, caller);
    }

public ASTNode rewriteTo() {
    return super.rewriteTo();
}

}

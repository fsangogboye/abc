package abc.aspectj.ast;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

import polyglot.ext.jl.ast.Node_c;


/** 
 * represent  ClassnamePatternExpr.SimpleNamePattern in pointcuts.
 * 
 * @author Oege de Moor
 */
public class ClassTypeDotId_c extends Node_c implements ClassTypeDotId
{
    protected ClassnamePatternExpr base;
    protected SimpleNamePattern name;
   
    public ClassTypeDotId_c(Position pos, 
			    ClassnamePatternExpr base,
			    SimpleNamePattern name)  {
	super(pos);
        this.base = base;
        this.name = name;
    }

    protected ClassTypeDotId_c reconstruct(ClassnamePatternExpr base,
					   SimpleNamePattern name) {
	if(base!=this.base || name!=this.name) {
	    ClassTypeDotId_c n = (ClassTypeDotId_c) copy();
	    n.base=base;
	    n.name=name;
	    return n;
	}
	return this;
    }

    public Node visitChildren(NodeVisitor v) {
	ClassnamePatternExpr base=(ClassnamePatternExpr) visitChild(this.base,v);
	SimpleNamePattern name=(SimpleNamePattern) visitChild(this.name,v);
	return reconstruct(base,name);
    }

    public ClassnamePatternExpr base() {
	return base;
    }

    public SimpleNamePattern name() {
	return name;
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
	if (name != null) 
	    w.write("(");
        print(base,w,tr);
        if (name != null) {
	    w.write(").");
	    print(name,w,tr);
        }
    }

    public String toString() {
	String s="";
	if(name !=null) s+="(";
	s+=base;
	if(name!=null) {
	    s+=")."+name;
	}
	return s;
    }

    public boolean equivalent(ClassTypeDotId c) {
	return (base.equivalent(c.base()) && name.equivalent(c.name()));
    }

}

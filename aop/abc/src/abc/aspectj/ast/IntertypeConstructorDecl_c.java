package abc.aspectj.ast;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import polyglot.ast.Node;
import polyglot.ast.Block;
import polyglot.ast.TypeNode;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.util.UniqueID;
import polyglot.util.Position;

import polyglot.visit.*;
import polyglot.types.*;

import polyglot.ext.jl.ast.ConstructorDecl_c;

import abc.aspectj.types.AspectJTypeSystem;
import abc.aspectj.visit.*;
import abc.aspectj.types.AJContext;
import abc.aspectj.types.InterTypeConstructorInstance_c;

public class IntertypeConstructorDecl_c extends ConstructorDecl_c
    implements IntertypeConstructorDecl, ContainsAspectInfo
{
    protected TypeNode host;

    public IntertypeConstructorDecl_c(Position pos,
                                 Flags flags,
                                 TypeNode host,
				 				 String name,
                                 List formals,
                                 List throwTypes,
	  	                 		Block body) {
	super(pos,flags,name,formals,throwTypes,body);
	this.host = host;
    }

	public TypeNode host() {
		return host;
	}
	
    protected IntertypeConstructorDecl_c reconstruct(List formals, 
						     List throwTypes, 
						     Block body,
						     TypeNode host) {
	if(host != this.host) {
	    IntertypeConstructorDecl_c n 
		= (IntertypeConstructorDecl_c) copy();
	    n.host = host;
	    return (IntertypeConstructorDecl_c) 
		n.reconstruct(formals,throwTypes,body);
	}
	return (IntertypeConstructorDecl_c) 
	    super.reconstruct(formals,throwTypes,body);
    }

    public Node visitChildren(NodeVisitor v) {
        List formals = visitList(this.formals, v);
        List throwTypes = visitList(this.throwTypes, v);
        Block body = (Block) visitChild(this.body, v);
		TypeNode host=(TypeNode) visitChild(this.host,v);
		return reconstruct(formals,throwTypes,body,host);
    }
    
    protected InterTypeConstructorInstance_c itConstructorInstance;
    
    /**
     * @author Aske Christensen
     * @author Oege de Moor
     * add itd of methods to host types
     */
    public NodeVisitor addMembersEnter(AddMemberVisitor am) {
		Type ht = host.type();
		if (ht instanceof ParsedClassType) {
		   AspectJTypeSystem ts = (AspectJTypeSystem) am.typeSystem();
		   ConstructorInstance ci = ts.interTypeConstructorInstance(position(),
		   							(ClassType) constructorInstance().container(),
		   							(ClassType) ht,
		   							constructorInstance().flags(),
		   							constructorInstance().formalTypes(),
		   							constructorInstance().throwTypes());
		   
	  	  ((ParsedClassType)ht).addConstructor(ci);
	  	  itConstructorInstance = (InterTypeConstructorInstance_c) ci;
		}
        return am.bypassChildren(this);
    }
    

	/**
	* @author Oege de Moor
	* change private intertype constructor decl into public,
	* mangling by giving it an extra parameter
	*/
	public IntertypeConstructorDecl accessChange(AspectJNodeFactory nf, AspectJTypeSystem ts) {
		if (flags().isPrivate() || flags().isPackage()) {
			ParsedClassType ht = (ParsedClassType) host.type();
			ht.fields().remove(itConstructorInstance); // remove old instance from host type    		
			ConstructorInstance mmi = itConstructorInstance.mangled();  // retrieve the mangled instance 		
			ht.addConstructor(mmi); // add new instance to host type 
            List newFormals = new LinkedList(formals());
            newFormals.add(itConstructorInstance.mangledFormal(nf,ts));
			return (IntertypeConstructorDecl) constructorInstance(mmi).flags(mmi.flags()).formals(newFormals);
		}
		return this;
	}
	

    /** Duplicate most of the things for ConstructorDecl here to avoid comparing
     *  the name against the contaning class.
     */
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Context c = tc.context();
        TypeSystem ts = tc.typeSystem();

        ClassType ct = c.currentClass();

		if (ct.flags().isInterface()) {
	    	throw new SemanticException(
			"Cannot declare an intertype constructor inside an interface.",
			position());
		}
		
		if (flags().isProtected()) {
			throw new SemanticException("Cannot declare a protected intertype constructor");
		}
		

        if (ct.isAnonymous()) {
	    throw new SemanticException(
		"Cannot declare an intertype constructor inside an anonymous class.",
		position());
        }

	/*
        String ctName = ct.name();

        if (! ctName.equals(name)) {
	    throw new SemanticException("Constructor name \"" + name +
                "\" does not match name of containing class \"" +
                ctName + "\".", position());
        }
	*/

	try {
	    ts.checkConstructorFlags(flags());
	}
	catch (SemanticException e) {
	    throw new SemanticException(e.getMessage(), position());
	}

	if (body == null && ! flags().isNative()) {
	    throw new SemanticException("Missing constructor body.",
		position());
	}

	if (body != null && flags().isNative()) {
	    throw new SemanticException(
		"A native constructor cannot have a body.", position());
	}

        for (Iterator i = throwTypes().iterator(); i.hasNext(); ) {
            TypeNode tn = (TypeNode) i.next();
            Type t = tn.type();
            if (! t.isThrowable()) {
                throw new SemanticException("Type \"" + t +
                    "\" is not a subclass of \"" + ts.Throwable() + "\".",
                    tn.position());
            }
        }

        return this;
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
	w.begin(0);
	w.write(flags.translate());
        print(host,w,tr);
        w.write(".new("); 

        w.begin(0);

	for (Iterator i = formals.iterator(); i.hasNext(); ) {
	    Formal f = (Formal) i.next();
	    print(f, w, tr);

	    if (i.hasNext()) {
		w.write(",");
		w.allowBreak(0, " ");
	    }
	}

	w.end();

	w.write(")");

	w.begin(0);

        if (! throwTypes().isEmpty()) {
	    w.allowBreak(6);
	    w.write("throws ");

	    for (Iterator i = throwTypes().iterator(); i.hasNext(); ) {
	        TypeNode tn = (TypeNode) i.next();
		print(tn, w, tr);

		if (i.hasNext()) {
		    w.write(",");
		    w.allowBreak(4, " ");
		}
	    }
	}

	w.end();

	if (body != null) {
	    printSubStmt(body, w, tr);
	}
	else {
	    w.write(";");
	}

	w.end();

    }
    
	/**
	* @author Oege de Moor
	* record the host type in the environment, for checking of this and super
	*/
	public Context enterScope(Context c) {
			AJContext nc = (AJContext) super.enterScope(c);
			TypeSystem ts = nc.typeSystem();
			return nc.pushHost(ts.staticTarget(host.type()).toClass(),
												flags().isStatic());
    }

    public void update(abc.weaving.aspectinfo.GlobalAspectInfo gai, abc.weaving.aspectinfo.Aspect current_aspect) {
	System.out.println("ICD host: "+host.toString());
	List formals = new ArrayList();
	Iterator fi = formals().iterator();
	while (fi.hasNext()) {
	    Formal f = (Formal)fi.next();
	    formals.add(new abc.weaving.aspectinfo.Formal(AspectInfoHarvester.toAbcType(f.type().type()),
							  f.name(), f.position()));
	}
	List exc = new ArrayList();
	Iterator ti = throwTypes().iterator();
	while (ti.hasNext()) {
	    TypeNode t = (TypeNode)ti.next();
	    exc.add(t.type().toString());
	}
	abc.weaving.aspectinfo.MethodSig impl = new abc.weaving.aspectinfo.MethodSig
	    (AspectInfoHarvester.convertModifiers(flags()),
	     current_aspect.getInstanceClass(),
	     new abc.weaving.aspectinfo.AbcType(soot.VoidType.v()),
	     name(),
	     formals,
	     exc,
	     position());
	abc.weaving.aspectinfo.MethodSig target = new abc.weaving.aspectinfo.MethodSig
	    (AspectInfoHarvester.convertModifiers(flags()),
	     gai.getClass(host.toString()),
	     new abc.weaving.aspectinfo.AbcType(soot.VoidType.v()),
	     name(),
	     formals,
	     exc,
	     null);
	abc.weaving.aspectinfo.IntertypeConstructorDecl icd = new abc.weaving.aspectinfo.IntertypeConstructorDecl
	    (target, impl, current_aspect, position());
	gai.addIntertypeConstructorDecl(icd);
    }
}
	

	

     



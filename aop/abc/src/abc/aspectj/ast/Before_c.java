package polyglot.ext.aspectj.ast;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import polyglot.ext.jl.types.TypeSystem_c;

import java.util.*;

public class Before_c extends AdviceSpec_c 
                              implements Before
{

    public Before_c(Position pos, 
                    List formals,
                    TypeNode voidn)
    {
	    super(pos);
        this.formals = formals;
        this.returnType = voidn;
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("before(");

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

    }

}
